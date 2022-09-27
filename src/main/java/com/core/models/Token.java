package com.core.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.web3j.abi.datatypes.Address;

import com.core.network.ERC20;
import com.core.network.NetworkConfig;

@Entity
public class Token extends PanacheEntity {

    public String name;

    public String symbol;

    @Column(updatable = false, unique = true)
    public String address;

    public int decimals;

    // public Token(String name, String symbol, String address, int decimals) {
    // this.name = name;
    // this.symbol = symbol;
    // this.address = address;
    // this.decimals = decimals;
    // }

    public static Token tokenFromAddress(NetworkConfig config, Address address) {
        Token token = new Token();

        ERC20 erc20TokenContract = ERC20.load(address.toString(), config.w3);
        try {
            token.decimals = erc20TokenContract.decimals().send().intValue();
            token.name = erc20TokenContract.name().send();
            token.address = address.toString();
            token.symbol = erc20TokenContract.symbol().send();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

}
