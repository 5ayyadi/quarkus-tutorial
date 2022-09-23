package com.gs;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.web3j.contracts.token.ERC20BasicInterface;
import org.web3j.contracts.token.ERC20Interface;

import java.math.BigDecimal;

public class Token extends PanacheEntity {
    public String name;
    public String symbol;
    public String address;

    public int decimals;

    /*
     * amount is an
     */
    public int amountWithDecimals(double amount) {
        // ERC20Interface
        // ERC20BasicInterface
        return 0;
    }

}
