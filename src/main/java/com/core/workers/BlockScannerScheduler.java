package com.core.workers;

import io.quarkus.logging.Log;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import com.core.customTypes.Address;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionObject;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;

import com.core.errors.BadTransactionHash;
import com.core.jobs.BlockScannerJob;
import com.core.jobs.ConfirmDepositsJob;
import com.core.models.TransactionStatus;
import com.core.models.TrxReceipt;
import com.core.models.block.ScannedBlocksStatus;
import com.core.models.block.ScannedBlocks;
import com.core.models.wallet.WalletExternalTransactions;
import com.core.network.GasStation;
import com.core.network.Network;
import com.core.repositories.TokenBalanceRepository;
import com.core.repositories.TokenRepository;
import com.core.repositories.TrxReceiptRepository;
import com.core.repositories.WalletRepository;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class BlockScannerScheduler {

    @ConfigProperty(name = "app.network")
    Network network;
    // TODO - Check if native Token exists in db ...
    private final AtomicBoolean checkNativeToken = new AtomicBoolean(false);
    private final AtomicBoolean isScannerRunning = new AtomicBoolean(false);
    private final AtomicBoolean isConfirmDepositsJobRunning = new AtomicBoolean(false);

    TokenRepository tokenRepository;
    WalletRepository walletRepository;
    TrxReceiptRepository trxReceiptRepository;
    TokenBalanceRepository tokenBalanceRepository;
    // JOBS ...
    BlockScannerJob blockScannerJob;
    ConfirmDepositsJob confirmDepositsJob;

    public BlockScannerScheduler(
            BlockScannerJob blockScannerJob,
            TokenRepository tokenRepository,
            WalletRepository walletRepository,
            ConfirmDepositsJob confirmDepositsJob,
            TrxReceiptRepository trxReceiptRepository,
            TokenBalanceRepository tokenBalanceRepository) {
        this.blockScannerJob = blockScannerJob;
        this.tokenRepository = tokenRepository;
        this.walletRepository = walletRepository;
        this.confirmDepositsJob = confirmDepositsJob;
        this.trxReceiptRepository = trxReceiptRepository;
        this.tokenBalanceRepository = tokenBalanceRepository;
        Log.info("BlockScanner Scheduler Started!");
    }

    @Transactional
    @Scheduled(every = "10s")
    void submitScannedBlocks() {
        if (!isConfirmDepositsJobRunning.get()) {
            try {
                isConfirmDepositsJobRunning.set(true);
                confirmDepositsJob.run();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isConfirmDepositsJobRunning.set(false);
            }
        }

    }

    @Transactional
    void blockScannerFailedBlocks() {
        blockScannerJob.checkFailedBlocks();
    }

    @Transactional
    void blockScannerMissedBlocks() {
        blockScannerJob.doubleCheckBlocks();
    }

    // @Scheduled(cron = "${blockScanner.BSC.ALL}")
    @Scheduled(every = "2s")
    @Transactional
    void blockScanner() {
        if (!isScannerRunning.get()) {
            isScannerRunning.set(true);
            try {
                blockScannerJob.run();
            } catch (Exception e) {
                // TODO: handle exception
                // e.printStackTrace();
                Log.error(e);
            } finally {
                isScannerRunning.set(false);
            }
        } else {
            Log.debug("Worker Is Busy");
        }
    }
}
