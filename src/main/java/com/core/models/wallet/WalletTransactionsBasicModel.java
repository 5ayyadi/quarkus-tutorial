package com.core.models.wallet;

import com.core.models.PanacheEntityWithTime;
import com.core.models.Token;
import com.google.inject.internal.MoreTypes.WildcardTypeImpl;

import java.math.BigInteger;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.MemoryHandler;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

class TransactionHistoryHandler extends MemoryHandler {
    // TODO - rabbit massage + db record ...
}

@MappedSuperclass
public abstract class WalletTransactionsBasicModel extends PanacheEntityWithTime {

    @ManyToOne
    @JoinColumn(name = "token_id", nullable = true)
    public Token token;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    public Wallet wallet;

    @Column(name = "amount")
    public BigInteger amount;

    public WalletTransactionStatus status;
    public WalletTransactionType type;

    public WalletTransactionsBasicModel() {
    }

    public WalletTransactionsBasicModel(Wallet wallet, String amount) {
        this(null, null, wallet, amount);
    }

    public WalletTransactionsBasicModel(Token token, Wallet wallet, String amount) {
        this(null, token, wallet, amount);
    }

    public WalletTransactionsBasicModel(Logger logger, Token token, Wallet wallet, String amount) {
        this(logger, token, wallet, new BigInteger(amount));
    }

    public WalletTransactionsBasicModel(Logger logger, Token token, Wallet wallet, BigInteger amount) {
        this.amount = amount;
        // this.logger = logger;
        this.wallet = wallet;
        this.token = token;
    }

    public abstract boolean transfer(Token token, Wallet toWallet, BigInteger amount);

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WalletTransactionsBasicModel that = (WalletTransactionsBasicModel) o;
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
}
