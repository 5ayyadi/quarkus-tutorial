package com.core.models;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.*;

import com.core.math.Decimal;
import com.core.models.wallet.Wallet;

@Table(name = "token_balance")
@Entity
public class TokenBalances extends PanacheEntityWithTime {

    @ManyToOne()
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @ManyToOne()
    @JoinColumn(name = "token_id", nullable = false)
    private Token token;

    @Column(length = 255, nullable = false)
    // @Column(precision = 100, scale = 0, nullable = true)
    private String Balance;

    @Column(length = 255, nullable = false)
    // @Column(precision = 100, scale = 0, nullable = true)
    private String ContractBalance;

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    @PrePersist
    private void balanceNotNullCheck() {
        if (Balance == null) {
            Balance = "0";
        }
        if (ContractBalance == null) {
            ContractBalance = "0";
        }
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public BigInteger getBalance() {
        // token.decimals
        return new BigInteger(Balance);
    }

    public BigDecimal getBalanceDecimaled() {
        return new Decimal(getBalance()).divideBydDecimal(token.decimals);
    }

    public Token getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "TokenBalances [Balance=" + Balance + ", token=" + token + ", wallet=" + wallet + "]";
    }

}
