package com.core.schemas.request;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Address;

import com.core.network.Network;

public class TokenBalancesRequest {
    public Network network;
    public String tokenAddress;
    public String walletAddress;
    public BigInteger amount;
}
