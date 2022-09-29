package com.core.network;

public enum Network {

    // BSCTestNet(new NetworkDetail("Binance Smart Chain testnet", "BSC", 60, 97,
    // new NetworkConfig("0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c",
    // "https://bsc-dataseed1.binance.org"))),
    BSC(new NetworkDetail("Binance Smart Chain", "BSC", 60, 56,
            new NetworkConfig("0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c", "https://bsc-dataseed1.binance.org"))),
    Rinkeby(new NetworkDetail("Ethereum Testnet Ropsten", "ETH", 60, 4,
            new NetworkConfig("0xDf032Bc4B9dC2782Bb09352007D4C57B75160B15", "https://rinkeby.infura.io/v3/"))),
    Ethereum(new NetworkDetail("Ethereum Network", "ETH", 60, 1,
            new NetworkConfig("0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2", "https://cloudflare-eth.com"))),
    EthereumLocal(new NetworkDetail("Ethereum Network", "ETH", 60, 1, new NetworkConfig()));

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
