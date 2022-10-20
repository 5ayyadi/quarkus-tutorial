package com.gs;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.core.models.TransactionStatus;
import com.core.models.wallet.WalletInternalTransactions;
import com.core.schemas.request.TransferRequest;
import com.core.schemas.request.WithdrawDepositRequest;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

public class Transfer {

    public static WalletInternalTransactions transfer(TransferRequest request,
            TokenRepository tokenRepo,
            WalletRepository walletRepository,
            WalletInternalTransactionRepository wltTrxRepo,
            TokenBalanceRepository tokenBalanceRepository) {
        WalletInternalTransactions trx = new WalletInternalTransactions(request, tokenRepo, walletRepository);
        trx.changeStatus(TransactionStatus.PENDING, wltTrxRepo);
        if (trx.isTransferValid(tokenBalanceRepository)) {
            // some code
            trx.fromWallet.withdraw(request, tokenBalanceRepository);
            trx.toWallet.deposit(request, tokenBalanceRepository);
            trx.changeStatus(TransactionStatus.SUCCESS, wltTrxRepo);
            return trx;
        }
        trx.changeStatus(TransactionStatus.FAILED, "Insufficient Balance", wltTrxRepo);
        return null;
    }
}