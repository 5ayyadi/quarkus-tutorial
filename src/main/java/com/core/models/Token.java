package com.core.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.logging.Log;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.web3j.abi.datatypes.Address;

import com.core.network.ERC20;
import com.core.network.NetworkConfig;

@Entity
public class Token extends PanacheEntityBase {

    @Id
    @GeneratedValue
    @Column(name = "token_id")
    private Long id;

    public String name;

    public String symbol;

    @Column(updatable = false, unique = true, name = "address")
    public String address;

    public int decimals;


    @ElementCollection
    @JoinTable(name = "token_balances",joinColumns=@JoinColumn(name="token_id"))
    private Set<TokenBalances> tokenBalances = new HashSet<TokenBalances>();

    

    // public Token(String name, String symbol, String address, int decimals) {
    // this.name = name;
    // this.symbol = symbol;
    // this.address = address;
    // this.decimals = decimals;
    // }

    public static Token tokenFromAddress(NetworkConfig config, String address) {
        return tokenFromAddress(config, new Address(address));
    }
    

    public static Token tokenFromAddress(NetworkConfig config, Address address) {

        Token token = new Token();

        ERC20 erc20TokenContract = ERC20.load(address.toString(), config.w3);
        try {
            Log.infof(" ERC20 contract validation %b", erc20TokenContract.isValid());
            token.decimals = erc20TokenContract.decimals().send().intValue();
            token.name = erc20TokenContract.name().send();
            token.address = address.toString();
            token.symbol = erc20TokenContract.symbol().send();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    public void setTokenBalances(Set<TokenBalances> tokenBalances) {
        this.tokenBalances = tokenBalances;
    }

    public Set<TokenBalances> getTokenBalances() {
        return tokenBalances;
    }

}
