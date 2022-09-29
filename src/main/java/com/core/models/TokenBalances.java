package com.core.models;

import javax.persistence.*;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Table(name = "token_balance")
@Entity
public class TokenBalances extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "token_id", nullable = false)
    private Token token;

    @Column(length = 255)
    private String Balance;

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }
}
