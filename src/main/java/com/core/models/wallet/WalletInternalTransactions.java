package com.core.models.wallet;

import java.math.BigInteger;

import javax.persistence.Entity;

import com.core.models.Token;

@Entity
public class WalletInternalTransactions extends WalletTransactionsBasicModel {

    public WalletInternalTransactions() {
    }

    public WalletInternalTransactions(Wallet wallet, String string) {
        super(wallet, string);
    }

    @Override
    public boolean transfer(Token token, Wallet toWallet, BigInteger amount) {
        // TODO Auto-generated method stub
        return false;
    }

    // @Override
    // public boolean transfer(Token token, Wallet toWallet, Decimal amount) {
    // return false;
    // }

}
