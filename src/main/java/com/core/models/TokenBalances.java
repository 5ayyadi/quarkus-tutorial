package com.core.models;

import javax.persistence.*;

import com.core.math.Decimal;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class TokenBalances extends PanacheEntity {
 
    @EmbeddedId
    private TokenBalancesId id = new TokenBalancesId();
 
    @ManyToOne
    @MapsId("tokenId")
    private Token token;
 
    @ManyToOne
    @MapsId("walletId")
    private Wallet wallet;

    @Column(name = "balance")
    public Decimal balance;

    public void setId(TokenBalancesId id) {
        this.id = id;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public void setBalance(Decimal balance) {
        this.balance = balance;
    }

    public TokenBalancesId getId() {
        return id;
    }

    public Token getToken() {
        return token;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public Decimal getBalance() {
        return balance;
    } 
    
}
