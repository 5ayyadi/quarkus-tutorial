package com.core.workers;

import io.quarkus.logging.Log;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

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
import com.core.models.block.BlockScanStatus;
import com.core.models.block.ScannedBlocks;
import com.core.models.wallet.Wallet;
import com.core.models.wallet.WalletExternalTransactions;
import com.core.network.BlockScanner;
import com.core.network.GasStation;
import com.core.network.Network;
import com.core.schemas.request.WithdrawDepositRequest;
import com.core.wallet.Deposit;
import com.gs.TokenBalanceRepository;
import com.gs.TokenRepository;
import com.gs.TrxReceiptRepository;
import com.gs.WalletRepository;

import io.quarkus.scheduler.Scheduled;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class BlockScannerScheduler {
    // @Inject
    // ScannedBlocks scannedBlocks;

    @ConfigProperty(name = "app.network")
    Network network;
    @ConfigProperty(name = "app.maxBlocksPerQuery")
    int maxBlocksPerQuery = 10;
    @ConfigProperty(name = "app.pendingBlocks")
    int pendingBlocks = 10;

    private AtomicInteger counter = new AtomicInteger();
    private AtomicBoolean isScannerRunning = new AtomicBoolean(false);

    TokenRepository tokenRepository;
    WalletRepository walletRepository;
    TrxReceiptRepository trxReceiptRepository;
    TokenBalanceRepository tokenBalanceRepository;

    @Channel("blockStatus")
    Emitter<String> blockStatusEmitter;

    // ReactiveMessagingExtension
    // Emitter<BitcoinMessage<?>> emitter;

    public BlockScannerScheduler(TokenRepository tokenRepository, WalletRepository walletRepository,
            TrxReceiptRepository trxReceiptRepository,
            TokenBalanceRepository tokenBalanceRepository) {
        this.tokenRepository = tokenRepository;
        this.walletRepository = walletRepository;
        this.trxReceiptRepository = trxReceiptRepository;
        this.tokenBalanceRepository = tokenBalanceRepository;
    }

    public int get() {
        return counter.get();
    }

    @Transactional
    void submitScannedBlocks() {

        for (TrxReceipt trxReceipt : trxReceiptRepository.parsedTrxReceipts()) {
            WalletExternalTransactions wet = new WalletExternalTransactions();
            wet.fromWallet = walletRepository.findByAddress(new Address(trxReceipt.fromAddress));
            if (trxReceipt.isErc20) {

                wet.token = tokenRepository.getByAddress(trxReceipt.toAddress);
                wet.amount = trxReceipt.amount;
                wet.toWallet = walletRepository.findByAddress(trxReceipt.getERC20ReceiverAddress());
            } else {
                wet.token = network.nativeToken;
                wet.amount = trxReceipt.amount;
                wet.toWallet = walletRepository.findByAddress(new Address(trxReceipt.toAddress));
            }
            wet.type = trxReceipt.trxType;
            wet.status = TransactionStatus.CONFIRMED;
            trxReceipt.status = TransactionStatus.CONFIRMED;
            // WithdrawDepositRequest request =
            // WithdrawDepositRequest.toWithdrawDepositRequest(wet);
            // switch (trxReceipt.trxType) {
            // case DEPOSIT: {
            // if (Deposit.isValid(
            // trxReceipt.transactionHash,
            // new Address(wet.token.address),
            // wet.amount,
            // network,
            // wet.toWallet)) {

            // wet.toWallet.deposit(request, tokenBalanceRepository,
            // tokenRepository,
            // walletRepository);
            // }
            // break;
            // }
            // case WITHDRAW: {
            // request.changeStatus(TransactionStatus.PENDING);
            // Wallet resWallet = walletRepository.findByUserId(request.userId);
            // if (resWallet.hasBalance(request, tokenBalanceRepository)) {
            // // withdraw in blockchain
            // // blockchain.withdraw(request);
            // resWallet.withdraw(request, tokenBalanceRepository);

            // }
            // break;

            // }
            // default:
            // break;
            // }

        }
    }

    void blockScannerFailedBlocks() {
        // TODO Query Failed Blocks and retry scanning them
    }

    @Scheduled(every = "1s")
    @Transactional
    void blockScanner() {
        Map<Address, Long> wallets = walletRepository.allWalletAddressMapping();
        Map<String, Long> tokens = tokenRepository.allTokenAddressMapping();

        if (isScannerRunning.get() == false) {
            isScannerRunning.set(true);
            try {
                Web3j w3 = network.value.w3;
                Long lastBlockNumber = (w3.ethBlockNumber().send().getBlockNumber().longValue()) - pendingBlocks;
                Long lastScannedBlocknumber = ScannedBlocks.lastScannedBlock();
                Long lastBlockToScan = lastScannedBlocknumber
                        + (maxBlocksPerQuery < (lastBlockNumber - lastScannedBlocknumber) ? maxBlocksPerQuery
                                : lastBlockNumber - lastScannedBlocknumber);

                if (lastScannedBlocknumber < lastBlockNumber) {

                    Log.debugf("Current Block Number : %s", lastBlockNumber);
                    Log.debugf("Last Scanned Block Number : %s", lastScannedBlocknumber);
                    BlockScanner blockScanner = new BlockScanner(wallets, tokens, network, tokenRepository);
                    GasStation gasStation = new GasStation(network);

                    for (Long i = lastScannedBlocknumber + 1; i <= lastBlockToScan; i++) {
                        // TODO Check If you have't fetched it before
                        Set<TrxReceipt> setOfTrxReceipts = new HashSet<>();
                        Block block = blockScanner.scanBlock(i);
                        ScannedBlocks scannedBlock = new ScannedBlocks(
                                block.getNumber().longValue(),
                                network,
                                BlockScanStatus.IN_PROGRESS,
                                block.getTransactions().size(),
                                new Timestamp(block.getTimestamp().longValue()));
                        blockStatusEmitter.send(scannedBlock.toString()).whenComplete((x, y) -> {
                            System.out.println(String.format("Completed %s , %s", x, y));
                        });
                        try {

                            for (TransactionResult<TransactionObject> trxObject : block.getTransactions()) {
                                try {
                                    TrxReceipt trxReceipt = blockScanner.processTrx(
                                            scannedBlock,
                                            trxObject.get(),
                                            gasStation);

                                    if (trxReceipt != null) {
                                        trxReceipt.persist();
                                        setOfTrxReceipts.add(trxReceipt);
                                        Log.debug(trxReceipt.toString());
                                        // Log.infof("TRX from:%s\tto:%s\tr:%s", trxReceipt.fromAddress,
                                        // trxReceipt.toAddress,
                                        // trxReceipt.getERC20ReceiverAddress());
                                    }
                                } catch (BadTransactionHash e) {
                                    // TODO: handle exception
                                    continue;
                                }

                            }

                            Log.debugf("BlockNumber#%d: Safely Quired %d transactions! \t persisted %d records!",
                                    block.getNumber(), block.getTransactions().size(), setOfTrxReceipts.size());

                            scannedBlock.getTrxs().addAll(setOfTrxReceipts);
                            scannedBlock.scanStatus = BlockScanStatus.SUCCESS;
                            scannedBlock.persist();
                            Log.debugf("Scanned-Block : %s", scannedBlock);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.errorf("Scanning-Block : %s   --due to : %s", scannedBlock, e);
                            scannedBlock.scanStatus = BlockScanStatus.FAILED;
                            scannedBlock.persist();
                        }
                    }
                }
                Log.debugf("HEAVY TASK FINISHED!");

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            } finally {
                isScannerRunning.set(false);
            }
        } else {
            Log.debug("Worker Is Busy");
        }
    }
}
