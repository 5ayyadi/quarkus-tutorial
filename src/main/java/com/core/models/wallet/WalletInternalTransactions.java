package com.core.models.wallet;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.core.models.Token;

@Entity
public class WalletInternalTransactions extends WalletTransactionsBasicModel {

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    public Wallet toWallet;

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

    public static List<WalletInternalTransactions> findByStatus(WalletTransactionStatus status) {
        return list("status", status);
    }
    // @Override
    // public boolean transfer(Token token, Wallet toWallet, Decimal amount) {
    // return false;
    // }

}
