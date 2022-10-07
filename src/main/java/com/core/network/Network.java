package com.core.network;

import java.util.logging.Logger;

public enum Network {

        // BSCTestNet(new NetworkDetail("Binance Smart Chain testnet", "BSC", 60, 97,
        // new NetworkConfig("0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c",
        // "https://bsc-dataseed1.binance.org"))),
        BSC(new NetworkDetail("Binance Smart Chain", "BSC", 60, 56,
                        new NetworkConfig("0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c",
                                        "https://bsc-dataseed1.binance.org"))),
        Rinkeby(new NetworkDetail("Ethereum Testnet Ropsten", "ETH", 60, 4,
                        new NetworkConfig("0xDf032Bc4B9dC2782Bb09352007D4C57B75160B15",
                                        "https://rinkeby.infura.io/v3/"))),
        Ethereum(new NetworkDetail("Ethereum Network", "ETH", 60, 1,
                        new NetworkConfig("0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2", "https://cloudflare-eth.com"))),
        EthereumLocal(new NetworkDetail("Ethereum Network", "ETH", 60, 1, new NetworkConfig())),
        Goerli(new NetworkDetail("Ethereum Testnet Goerli", "ETH", 60, 5,
                        new NetworkConfig("0xb4fbf271143f4fbf7b91a5ded31805e42b2208d6",
                                        "https://goerli.infura.io/v3/9aa3d95b3bc440fa88ea12eaa4456161"))),
        Fantom(new NetworkDetail("Fantom Opera", "FTM", 60, 250, new NetworkConfig(
                        "0xb4fbf271143f4fbf7b91a5ded31805e42b2208d6", "https://rpc.ankr.com/fantom/")));

        public final NetworkDetail value;
        public final Logger logger;

        Network(NetworkDetail value) {
                this.value = value;
                String loggerName = String.format("NetworkLogger:%s", value.name);
                this.logger = Logger.getLogger(loggerName);
        }

        Network(NetworkDetail value, Logger logger) {
                this.value = value;
                this.logger = logger;
        }

        @Override
        public String toString() {
                return "Network{" +
                                "value=" + value +
                                '}';
        }
}
