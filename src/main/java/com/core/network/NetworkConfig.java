package com.core.network;

import org.web3j.abi.datatypes.Address;

import com.gs.Token;

import io.quarkus.logging.Log;

import java.math.BigInteger;
import java.util.List;

public class NetworkConfig {
    public Address WrappedToken;
    public List<String> RPCList;
    public List<String> webSocketList;

    public NetworkConfig() {

    }

    public static Token tokenDetails(Address address, String RPC, String PrivateKey){
        Token token = new Token(address.toString(),RPC,PrivateKey);
        return token;
    }

    // TODO: Big integer to change to decimal
    public BigInteger getBalance(Address address, String RPC, String PrivateKey){
        Token token = new Token(address.toString(),RPC , PrivateKey);
        String owner = ""; // = getPublicKey(PrivateKey) // TODO: some function that converts private key to public
        try{
            return token.contract.balanceOf(owner).send();
        }
        catch(Exception e){
            Log.errorf("NetworkConfig at getBalance, Exception: ", e);
            return null;
        }
    }

}
