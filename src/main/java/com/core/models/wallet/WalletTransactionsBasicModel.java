package com.core.models.wallet;

import com.core.models.PanacheEntityWithTime;
import com.core.models.Token;
import com.core.models.TransactionStatus;
import com.core.models.TransactionType;
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
    public Wallet fromWallet;

    @ManyToOne
    @JoinColumn(name = "to_wallet")
    public Wallet toWallet;

    @Column(name = "amount")
    public BigInteger amount;

    public String message;
    public TransactionStatus status;
    public TransactionType type;

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
        this.fromWallet = wallet;
        this.token = token;
    }

    public abstract boolean transfer(Token token, Wallet toWallet, BigInteger amount);

    public Token getToken() {
        return token;
    }
}
