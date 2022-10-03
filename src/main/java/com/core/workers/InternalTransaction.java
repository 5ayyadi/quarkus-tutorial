package com.core.workers;

import java.math.BigInteger;
import java.util.List;

import com.core.models.TokenBalances;
import com.core.models.TransactionStatus;
import com.core.models.wallet.*;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

public class InternalTransaction extends PanacheEntity {

    public void confirmedTransactions() {
        List<WalletInternalTransactions> confirmedTransactions = WalletInternalTransactions
                .findByStatus(WalletTransactionStatus.CONFIRMED);
        for (WalletInternalTransactions trx : confirmedTransactions) {
            switch (trx.type) {
                case TRANSFER:
                    Wallet source = trx.wallet;
                    Wallet destination = trx.toWallet;
                    TokenBalances sourceBalance = source.getTokenBalances(trx.token);
                    TokenBalances destinationBalance = destination.getTokenBalances(trx.token);
                    BigInteger sb = new BigInteger(sourceBalance.getBalance());
                    BigInteger db = new BigInteger(destinationBalance.getBalance());
                    db.add(trx.amount);
                    sb.subtract(trx.amount);
                    break;
                case DEPOSIT:
                    destination = trx.toWallet;
                    destinationBalance = destination.getTokenBalances(trx.token);
                    db = new BigInteger(destinationBalance.getBalance());
                    db.add(trx.amount);
                    break;
                case WITHDRAW:
                    source = trx.wallet;
                    sourceBalance = source.getTokenBalances(trx.token);
                    sb = new BigInteger(sourceBalance.getBalance());
                    sb.subtract(trx.amount);
            }
            trx.status = WalletTransactionStatus.SUCCESS;
            trx.persist();
        }

    }

}
