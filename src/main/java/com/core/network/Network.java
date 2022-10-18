package com.core.network;

import java.util.logging.Logger;

import org.web3j.protocol.Web3j;

import com.core.models.Token;

public enum Network {

        BSC(new NetworkDetail("Binance Smart Chain", "BSC", 60, 56,
                        new NetworkConfig("0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c",
                                        "https://bsc-dataseed1.binance.org"),
                        "wbnb")),
        BSCTestNet(new NetworkDetail("Binance Smart Chain testnet", "tBSC", 60, 97,
                        new NetworkConfig("0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c",
                                        "https://data-seed-prebsc-1-s1.binance.org:8545/"),
                        "wbnb")),
        Rinkeby(new NetworkDetail("Ethereum Testnet Ropsten", "ETH", 60, 4,
                        new NetworkConfig("0xDf032Bc4B9dC2782Bb09352007D4C57B75160B15",
                                        "https://rinkeby.infura.io/v3/"),
                        "weth")),
        Ethereum(new NetworkDetail("Ethereum Network", "ETH", 60, 1,
                        new NetworkConfig("0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2", "https://cloudflare-eth.com"),
                        "weth")),
        EthereumLocal(new NetworkDetail("Ethereum Network", "ETH", 60, 1, new NetworkConfig(), "weth")),
        Goerli(new NetworkDetail("Ethereum Testnet Goerli", "ETH", 60, 5,
                        new NetworkConfig("0xb4fbf271143f4fbf7b91a5ded31805e42b2208d6",
                                        "https://goerli.infura.io/v3/9aa3d95b3bc440fa88ea12eaa4456161"),
                        "weth")),
        Fantom(new NetworkDetail("Fantom Opera", "FTM", 60, 250, new NetworkConfig(
                        "0xb4fbf271143f4fbf7b91a5ded31805e42b2208d6", "https://rpc.ankr.com/fantom/"),
                        "wrapped-fantom")),
        FtmTestnet(new NetworkDetail("Fantom Testnet", "FTM", 60, 0xfa2, new NetworkConfig(
                        "0x07b9c47452c41e8e00f98ac4c075f5c443281d2a", "https://rpc.testnet.fantom.network/"),
                        "wrapped-fantom"));

        public final NetworkDetail value;
        public final Logger logger;
        public final Web3j w3;
        public final Token nativeToken;

        Network(NetworkDetail value) {
                this.value = value;
                this.w3 = value.w3;
                String loggerName = String.format("NetworkLogger:%s", value.name);
                this.logger = Logger.getLogger(loggerName);
                // TODO
                this.nativeToken = null;
        }

        Network(NetworkDetail value, Logger logger) {
                this.value = value;
                this.w3 = value.w3;
                this.logger = logger;
                // TODO
                this.nativeToken = null;
        }

        @Override
        public String toString() {
                return "Network{" +
                                "value=" + value +
                                '}';
        }
}
