package com.core.network;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionObject;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;

import com.core.errors.BadTransactionHash;
import com.core.models.Token;
import com.core.models.TransactionStatus;
import com.core.models.TransactionType;
import com.core.models.TrxReceipt;
import com.core.models.block.ScannedBlocks;
import com.gs.TokenRepository;

import io.quarkus.logging.Log;

/*
* Used Via Worker to reads blocks one by one 
* fills TRXReceipt
* finds deposit  
* accepts withdrawal requests  
*/
// @ApplicationScoped
public final class BlockScanner {

    // @Inject
    @Transient
    TokenRepository tokenRepo;

    Map<Address, Long> wallets;
    Map<String, Long> tokens;
    Network network;

    public BlockScanner(Map<Address, Long> wallets, Map<String, Long> tokens, Network network,
            TokenRepository tokenRepo) {
        this.wallets = wallets;
        this.tokens = tokens;
        this.network = network;
        this.tokenRepo = tokenRepo;
    }

    /*
     * 1. Token Deposit :
     * - fromAddress is unknown
     * - toAddress is TokenAddress (Tries to fetch token)
     * - toAddress inside transaction data is a user_wallet
     * 2. Token Withdrawal :
     * - fromAddress is a wallet (A master wallet possibly)
     * - toAddress is TokenAddress (Tries to fetch token)
     * 3. ETH Deposit :
     * - fromAddress is unknown
     * - toAddress is user_wallet
     * 4. ETH Withdrawal :
     * - fromAddress is a wallet (A master wallet possibly)
     * - toAddress is user_wallet
     * 
     * If cant get amount from data or from ETH value in transaction
     */
    public TrxReceipt processTrx(ScannedBlocks scannedBlock, TransactionObject trxObject,
            GasStation gasStation) throws BadTransactionHash {

        TrxReceipt trxReceipt = TrxReceipt.castTransactionObjectToTrxReceipt(trxObject, scannedBlock,
                gasStation.nativeTokenPrice);
        trxReceipt.scannedBlock = scannedBlock;
        if (trxReceipt.toAddress == null) {
            // Contract Creation !
            // no need
            throw new BadTransactionHash(trxReceipt.transactionHash);
        }

        boolean shouldSave = false;
        Address toAddress = new Address(trxReceipt.toAddress);
        Address fromAddress = new Address(trxReceipt.fromAddress);

        // If trx to address is a token then this case happens ...
        // else it would be null
        // -USDC-> walletAddress ....

        if (wallets.containsKey(fromAddress)) {
            Long sender_wallet_id = wallets.get(fromAddress);
            Log.infof("Found From walletId of %s", sender_wallet_id);
            if (wallets.containsKey(toAddress)) {
                // #4
                Long receiver_wallet_id = wallets.get(fromAddress);
                Log.infof("Found To walletId of %s", receiver_wallet_id);
                trxReceipt.trxType = TransactionType.WITHDRAW;
                // trxReceipt.persist();
                shouldSave = true;
            } else if (tokens.containsKey(trxReceipt.toAddress)) {
                // #2
                trxReceipt.trxType = TransactionType.WITHDRAW;
                Long receiver_token_id = tokens.get(trxReceipt.fromAddress);
                Log.infof("Found To tokenId of %s", receiver_token_id);
                shouldSave = true;

            } else {
                Token token = tokenRepo.tokenFromAddress(network, new Address(trxReceipt.toAddress), true);
                if (token != null) {
                    Log.infof("Found New Token %s on trx: %s", token.symbol, trxReceipt.transactionHash);
                    trxReceipt.trxType = TransactionType.WITHDRAW;
                    Log.infof("Found To tokenId of %s", token.id);
                    shouldSave = true;
                }
            }
        } else if (tokens.containsKey(trxReceipt.fromAddress)) {
            // Kinda impossible case
            // Token
            Long token_id = tokens.get(trxReceipt.fromAddress);
            Log.infof("Found to tokenId of %s", token_id);
            trxReceipt.trxType = TransactionType.UNKNOWN;
            shouldSave = true;
        } else if (wallets.containsKey(toAddress)) {
            // #3
            // TODO ETH DEPOSIT ....
            Long receiver_token_id = tokens.get(trxReceipt.fromAddress);
            Log.infof("Found To tokenId of %s", receiver_token_id);
            trxReceipt.trxType = TransactionType.DEPOSIT;
            shouldSave = true;
        } else if (tokens.containsKey(trxReceipt.toAddress)) {
            // #1 (Probably) < PARSE TRX DATA : IF receiver there is wallet address >
            if (trxReceipt.getERC20ReceiverAddress() == null) {
                // Some Thing Went really wrong ....
                // Means the token address is a wallet address
                // FIXME - WHY ?

            } else {

                Long token_id = tokens.get(trxReceipt.toAddress);
                Log.infof("Found to tokenId of %s", token_id);
                trxReceipt.trxType = TransactionType.DEPOSIT;
                shouldSave = true;
            }
        } else if (trxReceipt.getERC20ReceiverAddress() != null) {
            if (wallets.containsKey(trxReceipt.getERC20ReceiverAddress())) {
                Long receiver_wallet_id = wallets.get(trxReceipt.getERC20ReceiverAddress());
                Log.infof("Found To walletId of %s", receiver_wallet_id);

                Long receiver_token_id = tokens.get(trxReceipt.fromAddress);
                if (receiver_token_id == null) {
                    Token token = tokenRepo.tokenFromAddress(network, new Address(trxReceipt.toAddress), true);
                    if (token != null)
                        receiver_token_id = token.id;

                }

                Log.infof("Found To tokenId of %s", receiver_token_id);
                trxReceipt.trxType = TransactionType.DEPOSIT;
                if (receiver_token_id == null)
                    trxReceipt.trxType = TransactionType.UNKNOWN;
                shouldSave = true;
            }
        }

        if (shouldSave) {
            trxReceipt.status = TransactionStatus.PARSED;
            return trxReceipt;

        } else {

            return null;
        }
    }

    public Block scanBlock(Long blockNumber) throws IOException {
        Web3j w3 = network.value.w3;
        DefaultBlockParameterNumber blockNumberObj = new DefaultBlockParameterNumber(blockNumber);
        return w3.ethGetBlockByNumber(blockNumberObj, true).send().getBlock();

    }

    public List<TransactionObject> fetchTransactionObjects(Block block) throws IOException {
        ArrayList<TransactionObject> trxObjects = new ArrayList<>();
        for (TransactionResult<TransactionObject> trx : block.getTransactions()) {
            trxObjects.add(trx.get());
        }
        return trxObjects;
    }
}
