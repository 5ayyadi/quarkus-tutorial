package com.core.models;

import javax.persistence.*;

import com.core.math.Decimal;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class TokenBalances extends PanacheEntity {

    public TokenBalances(Token token, Wallet wallet, Decimal balance){
        this.token = token;
        this.wallet = wallet;
        this.balance = balance;
    }
    
    @OneToMany
    @JoinColumn(name = "wallet")
    public Wallet wallet;

    @OneToMany
    @Column(name = "token")
    public Token token;

    
    @Column(name = "balance")
    public Decimal balance;

}
