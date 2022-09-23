package com.core.network;

public enum Network {

    BSCTestNet(new NetworkDetail("Binance Smart Chain testnet", "BSC", 60, 97)),
    BSC(new NetworkDetail("Binance Smart Chain", "BSC", 60, 56)),
    Ropsten(new NetworkDetail("Ethereum Testnet Ropsten", "ETH", 60, 3)),
    Ethereum(new NetworkDetail("Ethereum Network", "ETH", 60, 1));

    public final NetworkDetail value;

    Network(NetworkDetail value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Network{" +
                "value=" + value +
                '}';
    }
}
