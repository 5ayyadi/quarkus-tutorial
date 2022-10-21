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

import com.core.customTypes.Address;

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
    private String address;

    // tOdO - Should later be unique with constraint of network and address
    @Column(nullable = true)
    public Network network;

    public int decimals;

    public boolean verified = false;

    @OneToMany(mappedBy = "token")
    private final Set<TokenBalances> tokenBalances = new HashSet<TokenBalances>();

    public void addTokenBalances(TokenBalances tb) {
        tokenBalances.add(tb);
    }

    public Address getAddress() {
        return new Address(address);
    }

    public void setAddress(Address address) {
        this.address = address.toString();
    }

    public Token() {
    }

    public Token(String name, String symbol, Address address, int decimals, Network network) {
        this.name = name;
        this.symbol = symbol;
        this.address = address.toString();
        this.decimals = decimals;
        // this.chainId = chainId;
        this.network = network;
    }

    public ERC20 tokenContract() {
        return ERC20.load(getAddress(), network.value.w3);
    }

}
