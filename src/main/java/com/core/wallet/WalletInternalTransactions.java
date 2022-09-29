package com.core.wallet;

import javax.persistence.Entity;

import com.core.math.Decimal;
import com.core.models.Token;
import com.core.models.Wallet;

@Entity
public class WalletInternalTransactions extends WalletTransactions {

    public WalletInternalTransactions() {
    }

    public WalletInternalTransactions(Wallet wallet, String string) {
        super(wallet, string);
    }

    @Override
    public boolean transfer(Token token, Wallet toWallet, Decimal amount) {

        return false;
    }

}
