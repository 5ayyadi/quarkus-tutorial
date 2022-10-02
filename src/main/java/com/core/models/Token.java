package com.core.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.web3j.abi.datatypes.Address;

import com.core.network.ERC20;
import com.core.network.Network;

@Entity
public class Token extends PanacheEntity {

    public String name;

    public String symbol;

    @Column(updatable = false, unique = true)
    public String address;
    // tOdO - Should later be unique with constraint of network and address
    @Column(nullable = true)
    public Network network;

    public int decimals;

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

    public static Token tokenFromAddress(Network network, Address address) {
        Token token;
        // ToDo - Query Token First ...
        ERC20 erc20TokenContract = ERC20.load(address.toString(), network.value.config.w3);
        try {
            token = new Token(
                    erc20TokenContract.name().send(),
                    erc20TokenContract.symbol().send(),
                    address.toString(),
                    erc20TokenContract.decimals().send().intValue(),
                    network);

        } catch (Exception e) {
            e.printStackTrace();
            token = null;
        }
        return token;
    }

}