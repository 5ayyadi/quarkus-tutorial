package com.core.wallet;

import com.core.math.Decimal;
import com.core.models.Wallet;

public class WalletInternalTransactions extends WalletTransactions {
    public WalletInternalTransactions(Wallet wallet) {
        super(wallet);
    }

    @Override
    public boolean transfer(Wallet toWallet, Decimal amount) {

        return false;
    }

}
