package com.core.models.wallet;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.core.models.Token;
import com.core.models.TrxReceipt;

@Entity
public class WalletExternalTransactions extends WalletTransactionsBasicModel {

    @OneToOne
    @JoinColumn(name = "TrxReceipt_id")
    private TrxReceipt trxReceipt;

    public WalletExternalTransactions() {
    }
    // public WalletExternalTransactions(Wallet wallet) {
    // super(wallet);
    // }

    @Override
    public boolean transfer(Token token, Wallet toWallet, BigInteger amount) {
        // TODO Auto-generated method stub
        return false;
    }

    // @Override
    // public boolean transfer(Token token, Wallet toWallet, BigInteger value) {
    // try {
    // // token.contract.transfer(toWallet.publicKey, value.toBigInteger()).send();
    // } catch (Exception e) {
    // e.printStackTrace();
    // return false;
    // }
    // return true;

    // }
}
