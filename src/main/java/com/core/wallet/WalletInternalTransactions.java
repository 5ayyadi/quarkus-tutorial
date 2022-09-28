package com.core.wallet;

import java.util.List;

import com.core.math.Decimal;
import com.core.models.TransactionStatus;
import com.core.models.Wallet;

public class WalletInternalTransactions extends WalletTransactions {


    public WalletInternalTransactions(Wallet wallet) {
        super(wallet);
    }

    public static List<WalletInternalTransactions> findByStatus(TransactionStatus status) {
        return list("status", status);
    }

    // @Override
    public boolean transfer(Wallet toWallet, Decimal amount) {

        return false;
    }

}
