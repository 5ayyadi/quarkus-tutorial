package com.core.network;

import org.web3j.protocol.Web3j;

public class NetworkDetail {
    public String name;
    public String symbol;
    public int HDPathCoinType;
    public int chainId;
    public NetworkConfig config;
    public Web3j w3;

    public NetworkDetail(String name, String symbol, int HDPathCoinType, int chainId) {
        this(name, symbol, HDPathCoinType, chainId, null);
    }

    public NetworkDetail(String name, String symbol, int HDPathCoinType, int chainId, NetworkConfig config) {
        this.name = name;
        this.symbol = symbol;
        this.HDPathCoinType = HDPathCoinType;
        this.chainId = chainId;
        this.config = config;
        this.w3 = this.config.w3;
    }

    @Override
    public String toString() {
        return "NetworkDetail{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", HDPathCoinType=" + HDPathCoinType +
                ", chainId=" + chainId +
                '}';
    }
}
