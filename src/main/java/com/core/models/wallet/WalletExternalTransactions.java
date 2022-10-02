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

    @ManyToOne
    @JoinColumn(name = "token_id", nullable = true)
    Token token;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    Wallet wallet;

    @OneToOne
    @JoinColumn(name = "TrxReceipt_id")
    // @Column(nullable = true)
    private TrxReceipt trxReceipt;

    @Column(name = "amount")
    public String amount;

    // @CreationTimestamp
    // @Temporal(TemporalType.TIMESTAMP)
    // @Column(name = "create_date")
    // private Date createDate;

    // @UpdateTimestamp
    // @Temporal(TemporalType.TIMESTAMP)
    // @Column(name = "modify_date")
    // private Date modifyDate;

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
