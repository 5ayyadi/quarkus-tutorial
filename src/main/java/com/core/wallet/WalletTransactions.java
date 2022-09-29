package com.core.wallet;

import com.core.math.Decimal;
import com.core.models.Token;
import com.core.models.Wallet;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.sql.Date;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.MemoryHandler;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

class TransactionHistoryHandler extends MemoryHandler {
    // TODO - rabbit massage + db record ...
}

public abstract class WalletTransactions extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "token_id", nullable = true)
    Token token;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    Wallet wallet;

    @Column(name = "amount")
    public String amount;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    Logger logger;

    public WalletTransactions() {
    }

    public WalletTransactions(Wallet wallet, String amount) {
        this(null, null, wallet, amount);
    }

    public WalletTransactions(Token token, Wallet wallet, String amount) {
        this(null, token, wallet, amount);
    }

    public WalletTransactions(Logger logger, Token token, Wallet wallet, String amount) {
        this.amount = amount;
        this.logger = logger;
        this.wallet = wallet;
        this.token = token;
    }

    public abstract boolean transfer(Token token, Wallet toWallet, Decimal amount);

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WalletTransactions that = (WalletTransactions) o;
        return token.equals(that.token) && wallet.equals(that.wallet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, wallet);
    }

    public Wallet getWallet() {
        return wallet;
    }

    public Token getToken() {
        return token;
    }

    // public static WalletTransactions log(Token token2, Wallet wallet2, String
    // string) {

    // return null;
    // }

}
