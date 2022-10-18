package com.core.schemas.request;

import java.math.BigInteger;

import com.core.customTypes.Address;

import com.core.network.Network;

public class TokenBalancesRequest {
    public Network network;
    public Address tokenAddress;
    public Address walletAddress;
    public BigInteger amount;
}
