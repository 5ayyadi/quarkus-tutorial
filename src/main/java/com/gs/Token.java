package com.gs;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.logging.Log;


import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;



public class Token extends PanacheEntity {
    public String name;
    public String symbol;
    public String address;
    public ERC20 contract;
    public int decimals;

    public Token(String address, String RPC, String PK){
        this.address = address;
        Web3j web3j = Web3j.build(new HttpService(RPC));
        Credentials creds = Credentials.create(PK);
        contract = new ERC20(address, web3j, creds, new DefaultGasProvider());
        try{
            symbol = contract.symbol().send();
            name = contract.name().send();
            decimals = contract.decimals().send().intValue();
        }
        catch(Exception e) {
            Log.errorf("Error at line 35. Exception: ", e);
        }

    }

    /*
     * amount is an
     */
    public int amountWithDecimals(double amount) {
        // ERC20Interface
        // ERC20BasicInterface
        return 0;
    }

}
