package com.core.network;

import org.web3j.protocol.Web3j;

public class NetworkDetail {
    public String name;
    public int decimals;
    public String symbol;
    public int HDPathCoinType;
    public Integer chainId;
    public NetworkConfig config;
    public Web3j w3;
    public String coingeckoId;

    public NetworkDetail(String name, String symbol, int HDPathCoinType, int chainId, String coingeckoId) {
        this(name, symbol, HDPathCoinType, chainId, null, coingeckoId);
    }

    public NetworkDetail(String name, String symbol, int HDPathCoinType, int chainId, NetworkConfig config,
            String coingeckoId) {
        this(name, symbol, HDPathCoinType, chainId, config, coingeckoId, 18);

    }

    public NetworkDetail(String name, String symbol, int HDPathCoinType, int chainId, NetworkConfig config,
            String coingeckoId, int decimals) {
        this.name = name;
        this.symbol = symbol;
        this.HDPathCoinType = HDPathCoinType;
        this.chainId = chainId;
        this.config = config;
        this.w3 = this.config.w3;
        this.decimals = decimals;
        this.coingeckoId = coingeckoId;
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
