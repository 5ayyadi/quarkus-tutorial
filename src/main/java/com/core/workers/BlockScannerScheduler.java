package com.core.workers;

import io.quarkus.logging.Log;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.Web3j;

import com.core.models.Token;
import com.core.models.TransactionType;
import com.core.models.TrxReceipt;
import com.core.models.block.BlockScanStatus;
import com.core.models.block.ScannedBlocks;
import com.core.models.wallet.Wallet;
import com.core.network.BlockScanner;
import com.core.network.Network;
import com.gs.TokenRepository;
import com.gs.WalletRepository;

import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class BlockScannerScheduler {
    // @Inject
    // ScannedBlocks scannedBlocks;

    private AtomicInteger counter = new AtomicInteger();
    private AtomicBoolean isScannerRunning = new AtomicBoolean(false);

    TokenRepository tokenRepository;
    WalletRepository walletRepository;

    public BlockScannerScheduler(TokenRepository tokenRepository, WalletRepository walletRepository) {
        this.tokenRepository = tokenRepository;
        this.walletRepository = walletRepository;
    }

    public int get() {
        return counter.get();
    }

    // @Scheduled(every = "10s")
    // void increment() {
    // counter.incrementAndGet();
    // System.out.println(get());
    // }

    // @Scheduled(cron = "0 15 10 * * ?")
    // void cronJob(ScheduledExecution execution) {
    // counter.incrementAndGet();
    // System.out.println(execution.getScheduledFireTime());
    // }

    // @Scheduled(cron = "{blockScanner.BSC.ALL}")

    // private Map<String, Token> getAllTokens() {
    // HashMap<String, Token> tokens = new HashMap<>();
    // for (Token token : tokenRepository.listAll()) {
    // tokens.put(token.address, token);
    // }
    // return tokens;
    // }
    // private Map<String, Wallet> getAllWallets() {
    // HashMap<String, Wallet> wallets = new HashMap<>();
    // for (Wallet wallet : walletRepository.listAll()) {
    // wallets.put(wallet.address, wallet);
    // }
    // return wallets;
    // }

    @Scheduled(every = "10s")
    @Transactional
    void cronJobWithExpressionInConfig() {
        // TODO - TEST ERC20
        // NOTE Worked on Following Code for testing this
        /*
         * !SECTION
         * from web3 import Web3
         * w3 = Web3(Web3.HTTPProvider("http://127.0.0.1:8545/"))
         * assert w3.isConnected()
         * to = Web3.toChecksumAddress("0x4c97380af08e1ee1846f00f737c0e1121087fedd")
         * pk = "0xac0974bec39a17e36ba4a6b4d238ff944bacb478cbed5efcae784d7bf4f2ff80" #
         * LOCALHOST
         * pk = "0x83a74bcbcaaf079797593159aacbfa094564fec4358ea8a8bed6082cfa6f1da5" #
         * USER_ID #1
         * amount = 1 * 10 ** 18
         * def send_value(pk,to,amount):
         * a = w3.eth.account.privateKeyToAccount(pk)
         * n = w3.eth.get_transaction_count(a.address)
         * p = w3.eth.gas_price
         * trx = {"nonce":n,
         * "gasPrice":p,
         * "to":to,
         * "gas":3_000_000,
         * "value":amount,
         * "data":""}
         * 
         * return w3.eth.wait_for_transaction_receipt(w3.eth.send_raw_transaction(a.
         * sign_transaction(trx).rawTransaction))
         * send_value(pk,to,amount)
         */

        Map<String, Long> wallets = walletRepository.allWalletAddressMapping();
        Map<String, Long> tokens = tokenRepository.allTokenAddressMapping();

        if (isScannerRunning.get() == false) {
            isScannerRunning.set(true);
            try {
                Network network = Network.EthereumLocal;
                System.out.println("HEAVY TASK STARTED!");

                Web3j w3 = network.value.w3;
                Long currentBlockNumber = w3.ethBlockNumber().send().getBlockNumber().longValue();
                Long lastBlocknumber = ScannedBlocks.lastScannedBlock();

                if (lastBlocknumber < currentBlockNumber) {
                    Log.debugf("Current Block Number : %s", currentBlockNumber);
                    Log.debugf("Last Scanned Block Number : %s", lastBlocknumber);

                    for (var entry : BlockScanner.fetchBlockNumbers(network, lastBlocknumber, currentBlockNumber)
                            .entrySet()) {
                        Set<TrxReceipt> setOfTrxReceipts = new HashSet<>();
                        ScannedBlocks scannedBlock = new ScannedBlocks(entry.getKey(), network, BlockScanStatus.DONE,
                                entry.getValue().size());

                        for (TrxReceipt trxReceipt : entry.getValue()) {
                            Log.infof("TRX from:%s\tto:%s\tr:%s", trxReceipt.fromAddress, trxReceipt.toAddress,
                                    trxReceipt.getERC20ReceiverAddress());

                            /*
                             * 1. Token Deposit :
                             * - fromAddress is unknown
                             * - toAddress is TokenAddress
                             * - toAddress inside transaction data is a user_wallet
                             * 2. Token Withdrawal :
                             * - fromAddress is a wallet (A master wallet possibly)
                             * - toAddress is TokenAddress
                             * 3. ETH Deposit :
                             * - fromAddress is unknown
                             * - toAddress is user_wallet
                             * 4. ETH Withdrawal :
                             * - fromAddress is a wallet (A master wallet possibly)
                             * - toAddress is user_wallet
                             */
                            trxReceipt.blockNumberId = scannedBlock;
                            if (wallets.containsKey(trxReceipt.fromAddress)) {
                                Long sender_wallet_id = wallets.get(trxReceipt.fromAddress);
                                Log.infof("Found From walletId of %s", sender_wallet_id);
                                if (wallets.containsKey(trxReceipt.toAddress)) {
                                    // #4
                                    Long receiver_wallet_id = wallets.get(trxReceipt.fromAddress);
                                    Log.infof("Found To walletId of %s", receiver_wallet_id);
                                    trxReceipt.trxType = TransactionType.WITHDRAW;
                                    setOfTrxReceipts.add(trxReceipt);
                                    trxReceipt.persist();
                                } else if (tokens.containsKey(trxReceipt.toAddress)) {
                                    // #2
                                    trxReceipt.trxType = TransactionType.WITHDRAW;
                                    Long receiver_token_id = tokens.get(trxReceipt.fromAddress);
                                    Log.infof("Found To tokenId of %s", receiver_token_id);
                                    setOfTrxReceipts.add(trxReceipt);
                                    trxReceipt.persist();
                                }
                            } else if (tokens.containsKey(trxReceipt.fromAddress)) {
                                // Kinda impossible case
                                // Token
                                Long token_id = tokens.get(trxReceipt.fromAddress);
                                Log.infof("Found to tokenId of %s", token_id);
                                setOfTrxReceipts.add(trxReceipt);
                                trxReceipt.trxType = TransactionType.UNKNOWN;
                                trxReceipt.persist();
                            } else if (wallets.containsKey(trxReceipt.toAddress)) {
                                // #3
                                Long receiver_token_id = tokens.get(trxReceipt.fromAddress);
                                Log.infof("Found To tokenId of %s", receiver_token_id);
                                trxReceipt.trxType = TransactionType.DEPOSIT;
                                setOfTrxReceipts.add(trxReceipt);
                                trxReceipt.persist();
                            } else if (tokens.containsKey(trxReceipt.toAddress)) {
                                // #1 (Probably) < PARSE TRX DATA : IF receiver there is wallet address >
                                Address receiver_address = trxReceipt.getERC20ReceiverAddress().get();

                                if (receiver_address == null) {
                                    // Some Thing Went really wrong ....
                                    // Means the token address is a wallet address

                                } else if (wallets.containsKey(receiver_address.toString())) {
                                    Long receiver_wallet_id = wallets.get(receiver_address.toString());
                                    Log.infof("Found To walletId of %s", receiver_wallet_id);
                                    Long receiver_token_id = tokens.get(trxReceipt.fromAddress);
                                    Log.infof("Found To tokenId of %s", receiver_token_id);
                                    trxReceipt.trxType = TransactionType.DEPOSIT;
                                    setOfTrxReceipts.add(trxReceipt);
                                    trxReceipt.persist();
                                }
                            }

                        }

                        Log.infof("BlockNumber#%d: Safely Quired %d transactions!",
                                entry.getKey(), entry.getValue().size());

                        scannedBlock.getTrxs().addAll(setOfTrxReceipts);
                        scannedBlock.persist();
                        System.out.println(scannedBlock);
                    }
                }

                counter.incrementAndGet();

                System.out.println("HEAVY TASK FINISHED!");
                System.out.println(counter.get());

            } catch (Exception e) {
                // TODO: handle exception

                e.printStackTrace();
            } finally {
                isScannerRunning.set(false);
            }
        }
    }
}
