package com.core.schemas.request;

import java.math.BigInteger;

import com.core.customTypes.Address;

import com.core.network.Network;

public class TokenBalancesRequest {
    public Network network;
    public Address tokenAddress;
    // TODO: serverWallet field is unnecessary. 
    public Address serverWallet;
    public BigInteger amount;
}
