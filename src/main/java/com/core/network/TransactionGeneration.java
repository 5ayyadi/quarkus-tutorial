package com.core.network;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;

public class TransactionGeneration {

        public static RawTransaction sendETH(Network network, String fromWallet, String toWallet, BigInteger amount)
                        throws Exception {
                Web3j w3 = network.value.w3;
                BigInteger fromWalletBalance = w3
                                .ethGetBalance(fromWallet, DefaultBlockParameterName.LATEST).send().getBalance();
                if (amount.compareTo(fromWalletBalance) != -1) {
                        // Wallet ETH Balance Must be more than desired amount
                        throw new Exception();
                }
                System.out.println(String.format("Current Wallet %s Balance is %s", fromWallet, fromWalletBalance));
                EthGasPrice gasPrice = w3.ethGasPrice().send();
                EthBlockNumber blockNumber = w3.ethBlockNumber().send();
                EthGetTransactionCount nonce = w3.ethGetTransactionCount(
                                fromWallet, DefaultBlockParameterName.LATEST).send();
                BigInteger currentGasLimit = new BigInteger("30000000");
                RawTransaction transaction = RawTransaction
                                // .createEtherTransaction(nonce, gasPrice, gasLimit, to, value)
                                .createEtherTransaction(
                                                // fromWallet,
                                                nonce.getTransactionCount(),
                                                // nonce.getTransactionCount().add(BigInteger.ONE),
                                                gasPrice.getGasPrice(),
                                                currentGasLimit,
                                                toWallet,
                                                amount);

                // w3.ethSendTransaction(transaction);
                // transaction.

                // w3.ethSendTransaction(transaction);
                return transaction;

        }

}
