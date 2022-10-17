package com.core.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.logging.Log;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.web3j.abi.datatypes.Address;

import com.core.network.ERC20;
import com.core.network.Network;
import com.core.network.NetworkConfig;
import com.core.schemas.request.TokenRequest;

@Entity
@Table(name = "token", uniqueConstraints = {
        @UniqueConstraint(name = "unique_token", columnNames = { "address", "network" })
})
public class Token extends PanacheEntityWithTime {

    public String name;

    @Column(unique = true)
    public String symbol;

    @Column(updatable = false, unique = true)
    public String address;

    // tOdO - Should later be unique with constraint of network and address
    @Column(nullable = true)
    public Network network;

    public int decimals;

    public boolean verified = false;

    @OneToMany(mappedBy = "token")
    private Set<TokenBalances> tokenBalances = new HashSet<TokenBalances>();

    public void addTokenBalances(TokenBalances tb) {
        tokenBalances.add(tb);
    }

    // public void setTokenBalances(Set<TokenBalances> tokenBalances) {
    // this.tokenBalances = tokenBalances;
    // }
    // public Set<TokenBalances> getTokenBalances() {
    // return tokenBalances;
    // }

    public Token() {
    }

    public Token(String name, String symbol, String address, int decimals, Network network) {
        this.name = name;
        this.symbol = symbol;
        this.address = address;
        this.decimals = decimals;
        // this.chainId = chainId;
        this.network = network;
    }

    public ERC20 tokenContract() {
        return ERC20.load(address, network.value.w3);
    }

}
