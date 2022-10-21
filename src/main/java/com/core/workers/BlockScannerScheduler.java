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
import com.core.models.TransactionStatus;
import com.core.models.TrxReceipt;
import com.core.models.block.BlockScanStatus;
import com.core.models.block.ScannedBlocks;
import com.core.models.wallet.WalletExternalTransactions;
import com.core.network.BlockScanner;
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

    private final AtomicInteger counter = new AtomicInteger();
    private final AtomicBoolean isScannerRunning = new AtomicBoolean(false);

    TokenRepository tokenRepository;
    WalletRepository walletRepository;
    TrxReceiptRepository trxReceiptRepository;
    TokenBalanceRepository tokenBalanceRepository;
    BlockScanner blockScanner;

    public BlockScannerScheduler(
            BlockScanner blockScanner,
            TokenRepository tokenRepository,
            WalletRepository walletRepository,
            TrxReceiptRepository trxReceiptRepository,
            TokenBalanceRepository tokenBalanceRepository) {
        this.blockScanner = blockScanner;
        this.tokenRepository = tokenRepository;
        this.walletRepository = walletRepository;
        this.trxReceiptRepository = trxReceiptRepository;
        this.tokenBalanceRepository = tokenBalanceRepository;
        Log.info("BlockScanner Scheduler Started!");
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

    // @Scheduled(cron = "${blockScanner.BSC.ALL}")
    @Scheduled(every = "15s")
    @Transactional
    void blockScanner() {
        if (!isScannerRunning.get()) {
            isScannerRunning.set(true);
            try {

                blockScanner.run();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                Log.error(e);
            } finally {
                isScannerRunning.set(false);
            }
        } else {
            Log.debug("Worker Is Busy");
        }
    }
}
