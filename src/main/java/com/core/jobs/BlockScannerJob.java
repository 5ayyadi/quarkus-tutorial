package com.core.jobs;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.Transient;
import javax.transaction.Transactional;

import com.core.customTypes.Address;

import org.eclipse.microprofile.config.inject.ConfigProperty;
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
import com.core.models.block.ScannedBlocksStatus;
import com.core.models.block.ScannedBlocks;
import com.core.network.GasStation;
import com.core.network.Network;
import com.core.repositories.TokenRepository;
import com.core.repositories.WalletRepository;

import io.quarkus.logging.Log;

/*
* Used Via Worker to reads blocks one by one 
* fills TRXReceipt
* finds deposit  
* accepts withdrawal requests  
*/
@ApplicationScoped
public final class BlockScannerJob {

    @ConfigProperty(name = "app.network")
    Network defaultNetwork;
    @ConfigProperty(name = "app.maxBlocksPerQuery")
    public int maxBlocksPerQuery;
    @ConfigProperty(name = "app.pendingBlocks")
    public int pendingBlocks;

    @Inject
    TokenRepository tokenRepository;

    @Inject
    WalletRepository walletRepository;

    public BlockScannerJob() {

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
            GasStation gasStation, Network network) throws BadTransactionHash {
        Map<Address, Long> wallets = walletRepository.allWalletAddressMapping();
        Map<Address, Long> tokens = tokenRepository.allTokenAddressMapping();

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
        if (toAddress == new Address("0x4c97380af08e1ee1846f00f737c0e1121087fedd")) {
            System.out.println(toAddress);
        }
        if (toAddress.toString() == ("0x4c97380af08e1ee1846f00f737c0e1121087fedd")) {
            System.out.println(toAddress);
        }
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
            } else if (tokens.containsKey(toAddress)) {
                // #2
                trxReceipt.trxType = TransactionType.WITHDRAW;
                Long receiver_token_id = tokens.get(fromAddress);
                Log.infof("Found To tokenId of %s", receiver_token_id);
                shouldSave = true;

            } else {
                Token token = tokenRepository.tokenFromAddress(network, toAddress, true);
                if (token != null) {
                    Log.infof("Found New Token %s on trx: %s", token.symbol, trxReceipt.transactionHash);
                    trxReceipt.trxType = TransactionType.WITHDRAW;
                    Log.infof("Found To tokenId of %s", token.id);
                    shouldSave = true;
                }
            }
        } else if (tokens.containsKey(fromAddress)) {
            // Kinda impossible case
            // Token
            Long token_id = tokens.get(fromAddress);
            Log.infof("Found to tokenId of %s", token_id);
            trxReceipt.trxType = TransactionType.UNKNOWN;
            shouldSave = true;
        } else if (wallets.containsKey(toAddress)) {
            // #3
            // TODO ETH DEPOSIT ....
            Long receiver_token_id = tokens.get(fromAddress);
            Log.infof("Found To tokenId of %s", receiver_token_id);
            trxReceipt.trxType = TransactionType.DEPOSIT;
            shouldSave = true;
        } else if (tokens.containsKey(toAddress)) {
            // #1 (Probably) < PARSE TRX DATA : IF receiver there is wallet address >
            if (trxReceipt.getERC20ReceiverAddress() == null) {
                // Some Thing Went really wrong ....
                // Means the token address is a wallet address
                // FIXME - WHY ?

            } else {

                Long token_id = tokens.get(toAddress);
                Log.infof("Found to tokenId of %s", token_id);
                trxReceipt.trxType = TransactionType.DEPOSIT;
                shouldSave = true;
            }
        } else if (trxReceipt.getERC20ReceiverAddress() != null) {
            if (wallets.containsKey(trxReceipt.getERC20ReceiverAddress())) {
                Long receiver_wallet_id = wallets.get(trxReceipt.getERC20ReceiverAddress());
                Log.infof("Found To walletId of %s", receiver_wallet_id);

                Long receiver_token_id = tokens.get(fromAddress);
                if (receiver_token_id == null) {
                    Token token = tokenRepository.tokenFromAddress(network, new Address(trxReceipt.toAddress), true);
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

    public Block scanBlock(Network network, Long blockNumber) throws IOException {
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

    public void run()
            throws IOException {
        run(defaultNetwork);
    }

    @Transactional
    public void run(Network network)
            throws IOException {
        Map<Address, Long> wallets = walletRepository.allWalletAddressMapping();
        Map<Address, Long> tokens = tokenRepository.allTokenAddressMapping();

        Web3j w3 = network.value.w3;
        Long lastBlockNumber = (w3.ethBlockNumber().send().getBlockNumber().longValue()) - pendingBlocks;
        Long lastScannedBlocknumber = ScannedBlocks.lastScannedBlock(network);
        Long lastBlockToScan = lastScannedBlocknumber
                + (maxBlocksPerQuery < (lastBlockNumber - lastScannedBlocknumber) ? maxBlocksPerQuery
                        : lastBlockNumber - lastScannedBlocknumber);

        if (lastScannedBlocknumber < lastBlockNumber) {

            Log.debugf("Current Block Number : %s", lastBlockNumber);
            Log.debugf("Last Scanned Block Number : %s", lastScannedBlocknumber);

            Log.debugf("wallets:%s , tokens:%s, blockchain:%d , lastBlock:%d", wallets,
                    tokens, lastBlockNumber,
                    lastScannedBlocknumber);
            GasStation gasStation = new GasStation(network);

            for (Long i = lastScannedBlocknumber + 1; i <= lastBlockToScan; i++) {
                // TODO Check If you have't fetched it before
                Set<TrxReceipt> setOfTrxReceipts = new HashSet<>();
                Block block = scanBlock(network, i);
                ScannedBlocks scannedBlock = new ScannedBlocks(
                        block.getNumber().longValue(),
                        network,
                        ScannedBlocksStatus.IN_PROGRESS,
                        block.getTransactions().size(),
                        new Timestamp(block.getTimestamp().longValue()));
                scannedBlock.persist();

                // blockStatusEmitter.send(scannedBlock.toString()).whenComplete((x, y) -> {
                // Log.debugf("Completed %s , %s", x, y);
                // });
                try {

                    for (TransactionResult<TransactionObject> trxObject : block.getTransactions()) {
                        try {
                            TrxReceipt trxReceipt = processTrx(
                                    scannedBlock,
                                    trxObject.get(),
                                    gasStation,
                                    network);

                            if (trxReceipt != null) {
                                trxReceipt.persist();
                                setOfTrxReceipts.add(trxReceipt);
                                Log.debug(trxReceipt.toString());
                            }
                        } catch (BadTransactionHash e) {
                            // TODO: handle exception
                            continue;
                        }

                    }

                    Log.debugf("BlockNumber#%d: Safely Quired %d transactions! \t persisted %d records!",
                            block.getNumber(), block.getTransactions().size(), setOfTrxReceipts.size());

                    scannedBlock.getTrxs().addAll(setOfTrxReceipts);
                    scannedBlock.scanStatus = ScannedBlocksStatus.SUCCESS;
                    scannedBlock.persist();
                    Log.debugf("Scanned-Block : %s", scannedBlock);
                } catch (Exception e) {
                    // e.printStackTrace();
                    Log.errorf("Scanning-Block : %s   --due to : %s", scannedBlock, e);
                    scannedBlock.scanStatus = ScannedBlocksStatus.FAILED;
                    scannedBlock.getTrxs().clear();
                    scannedBlock.persist();
                }
            }
        }
        Log.debugf("HEAVY TASK FINISHED!");

    }

    public void doubleCheckBlocks() {
        // TODO check if there are any missing block numbers
        // Count from a max number of block in the past
    }

    public void checkFailedBlocks() {
        // TODO check if there are any missing block numbers
        // Count from a max number of block in the past
    }

}
