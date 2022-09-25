package com.core.wallet;

import com.core.math.Decimal;
import com.core.models.Token;
import com.core.models.Wallet;

import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.MemoryHandler;

class TransactionHistoryHandler extends MemoryHandler {
    // TODO - rabbit massage + db record ...
}

public abstract class WalletTransactions {
    Token token;
    Wallet wallet;
    Logger logger;

    protected WalletTransactions(Wallet wallet) {
        this(null, null, wallet);
    }

    protected WalletTransactions(Token token, Wallet wallet) {
        this(null, token, wallet);
    }

    protected WalletTransactions(Logger logger, Token token, Wallet wallet) {
        this.logger = logger;
        this.wallet = wallet;
        this.token = token;
    }

    public abstract boolean transfer(Wallet toWallet, Decimal amount);

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

}
