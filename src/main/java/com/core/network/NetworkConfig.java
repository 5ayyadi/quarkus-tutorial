package com.core.network;

import io.quarkus.resteasy.runtime.vertx.JsonObjectReader;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.core.models.Token;

import io.quarkus.logging.Log;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetworkConfig {
    public Address wrappedTokenAddress;
    public List<String> rpcList;
    public List<String> webSocketList;

    public Web3j w3;
    public StaticGasProvider gasProvider;

    public NetworkConfig() {
        this("0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2", HttpService.DEFAULT_URL);
    }

    public NetworkConfig(
            String wrappedTokenAddress,
            String rpc) {
        this(new Address(wrappedTokenAddress), rpc);
    }

    public NetworkConfig(
            Address wrappedTokenAddress,
            String rpc) {
        this(wrappedTokenAddress, Arrays.asList(rpc), new ArrayList<String>());
    }

    public NetworkConfig(
            Address wrappedTokenAddress,
            List<String> rpcList) {
        this(wrappedTokenAddress, rpcList, new ArrayList<String>());
    }

    public NetworkConfig(
            Address wrappedTokenAddress,
            List<String> rpcList,
            List<String> webSocketList) {
        this(wrappedTokenAddress, rpcList, webSocketList, new DefaultGasProvider());
    }

    public NetworkConfig(
            Address wrappedTokenAddress,
            List<String> rpcList,
            List<String> webSocketList,
            StaticGasProvider gasProvider) {
        this.wrappedTokenAddress = wrappedTokenAddress;
        this.rpcList = rpcList;
        this.webSocketList = webSocketList;
        this.w3 = null;
        if (rpcList.size() >= 1) {
            this.w3 = Web3j.build(new HttpService(rpcList.get(0)));
        }
        this.gasProvider = gasProvider;
    }

    // public static Token tokenDetails(Address address, String RPC, String
    // PrivateKey) {
    // Token token = new Token(address.toString(), RPC, PrivateKey);
    // return token;
    // }

    // // TODO: Big integer to change to decimal
    // public BigInteger getBalance(Address address, String RPC, String PrivateKey)
    // {
    // Token token = new Token(address.toString(), RPC, PrivateKey);
    // String owner = ""; // = getPublicKey(PrivateKey) // TODO: some function that
    // converts private key
    // // to public
    // try {
    // return token.contract.balanceOf(owner).send();
    // } catch (Exception e) {
    // Log.errorf("NetworkConfig at getBalance, Exception: ", e);
    // return null;
    // }
    // }

}
