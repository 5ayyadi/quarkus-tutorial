package com.api;

import com.core.models.TransactionStatus;
import com.core.models.wallet.WalletInternalTransactions;
import com.core.repositories.TokenBalanceRepository;
import com.core.repositories.TokenRepository;
import com.core.repositories.WalletInternalTransactionRepository;
import com.core.repositories.WalletRepository;
import com.core.schemas.request.TransferRequest;

public class Transfer {

    public static WalletInternalTransactions transfer(TransferRequest request,
            TokenRepository tokenRepo,
            WalletRepository walletRepository,
            WalletInternalTransactionRepository wltTrxRepo,
            TokenBalanceRepository tokenBalanceRepository) {
        WalletInternalTransactions trx = new WalletInternalTransactions(request, tokenRepo, walletRepository);
        trx.changeStatus(TransactionStatus.PENDING, wltTrxRepo);
        trx.persist();
        if (trx.isTransferValid(tokenBalanceRepository)) {
            // some code
            trx.fromWallet.withdraw(request, tokenBalanceRepository);
            trx.toWallet.deposit(request, tokenBalanceRepository);
            trx.changeStatus(TransactionStatus.SUCCESS, wltTrxRepo);
        } else {
            trx.changeStatus(TransactionStatus.FAILED, "Insufficient Balance", wltTrxRepo);

        }
        return trx;
    }
}