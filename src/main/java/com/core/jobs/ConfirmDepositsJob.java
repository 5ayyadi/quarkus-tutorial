package com.core.jobs;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.core.customTypes.Address;
import com.core.models.TransactionStatus;
import com.core.models.TrxReceipt;
import com.core.models.wallet.WalletExternalTransactions;
import com.core.models.wallet.WalletInternalTransactions;
import com.core.network.Network;
import com.core.repositories.TokenBalanceRepository;
import com.core.repositories.TokenRepository;
import com.core.repositories.TrxReceiptRepository;
import com.core.repositories.WalletInternalTransactionRepository;
import com.core.repositories.WalletRepository;

import io.quarkus.logging.Log;

@Singleton
public class ConfirmDepositsJob {

    @ConfigProperty(name = "app.network")
    Network network;

    @Inject
    WalletRepository walletRepository;

    @Inject
    TokenRepository tokenRepository;

    @Inject
    TrxReceiptRepository trxReceiptRepository;

    @Inject
    TokenBalanceRepository tokenBalanceRepository;

    @Inject
    WalletInternalTransactionRepository internalTransactionRepository;

    public void run() {

        for (TrxReceipt trxReceipt : trxReceiptRepository.parsedTrxReceipts()) {
            WalletExternalTransactions wet = new WalletExternalTransactions();

            wet.fromWallet = walletRepository.findByAddress(new Address(trxReceipt.fromAddress));

            if (trxReceipt.isErc20) {
                wet.token = tokenRepository.getByAddress(trxReceipt.toAddress);
                wet.amount = trxReceipt.amount;
                wet.toWallet = walletRepository.findByAddress(trxReceipt.getERC20ReceiverAddress());
            } else {
                wet.token = tokenRepository.getNetworkNativeToken(network);
                wet.amount = trxReceipt.amount;
                wet.toWallet = walletRepository.findByAddress(new Address(trxReceipt.toAddress));
            }

            wet.type = trxReceipt.trxType;
            wet.status = TransactionStatus.CONFIRMED;
            trxReceipt.status = TransactionStatus.CONFIRMED;
            WalletInternalTransactions wit = null;
            if (wit != null) {
                Log.warnf("DOUBLE SPENDING FOUND AT :: {'trxReceipt':'%s', 'wet':'%s' , 'wit':'%s' .....!",
                        trxReceipt, wet, wit);
            }
            switch (wet.type) {
                case DEPOSIT: {
                    wit = WalletInternalTransactions.fromDepositExtInternalTransactions(wet);
                    wit.toWallet.deposit(wit.token.id, wit.amount, tokenBalanceRepository);
                    wit.persist();
                    break;
                }
                case WITHDRAW: {
                    wit = WalletInternalTransactions.fromWithdrawExtInternalTransactions(wet);
                    wit.persist();
                    break;
                }
                default:
                    Log.errorf("unsupported trx type of %s", wet.type);
            }
            // internalTransactionRepository.

        }

    }
}
