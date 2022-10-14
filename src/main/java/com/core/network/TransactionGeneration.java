package com.core.network;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.utils.Strings;

import com.core.models.Token;

public class TransactionGeneration {

        private static final BigInteger DEFAULT_GAS_LIMIT = new BigInteger("30000000");
        private static final String ERC20TRANSFER_FUNCTION_SELECTOR = "0xa9059cbb";
        private static final BigInteger MAX_ALLOWED_INT = new BigInteger(
                        "115792089237316195423570985008687907853269984665640564039457584007913129639935");

        /**
         * RawTransaction
         * +
         * In Which blocknumber was this generated!
         */
        public static class RawTransactionAndExtraInfo {
                public BigInteger requestedBlockNumber;
                public RawTransaction rawTransaction;
                public GasStation gasStation;
                public Network network;

                public RawTransactionAndExtraInfo(RawTransaction rawTransaction, BigInteger requestedBlockNumber,
                                Network network, GasStation gasStation) {
                        this.rawTransaction = rawTransaction;
                        this.requestedBlockNumber = requestedBlockNumber;
                        this.network = network;
                        this.gasStation = gasStation;
                }

        }

        public static RawTransactionAndExtraInfo transferETH(Network network, String fromWallet, String toWallet,
                        BigInteger amount)
                        throws Exception {
                return TransactionGeneration.transferETH(network, fromWallet, toWallet, amount, DEFAULT_GAS_LIMIT);
        }

        public static String transferTrxDataGenerator(String functionSelector, String toWallet, BigInteger amount)
                        throws Exception {
                return TransactionGeneration.transferTrxDataGenerator(functionSelector, new Address(toWallet), amount);
        }

        public static String transferTrxDataGenerator(String functionSelector, Address toWallet, BigInteger amount)
                        throws Exception {
                // ATTENTION - I Assume function selector is Always OK so ...
                if (amount.compareTo(MAX_ALLOWED_INT) != -1) {
                        // TODO - only supports 2^256 -1
                        throw new Exception();
                }
                String _toWallet = toWallet.getValue().substring(2);
                int _toWalletZerosNeeded = 64 - _toWallet.length();

                String _amount = amount.toString(16);
                int _amountZerosNeeded = 64 - _amount.length();

                return String.format("%s%s%s",
                                functionSelector,
                                (Strings.zeros(_toWalletZerosNeeded) + _toWallet),
                                (Strings.zeros(_amountZerosNeeded) + _amount));

        }

        /**
         * Generates simple Raw Transaction object for sending value from one address to
         * another
         * 
         * @param network
         * @param fromWallet
         * @param toWallet
         * @param amount
         * @param currentGasLimit
         * @return RawTransactionAndExtraInfo
         * @throws Exception
         */
        public static RawTransactionAndExtraInfo transferETH(Network network, String fromWallet, String toWallet,
                        BigInteger amount, BigInteger currentGasLimit)
                        throws Exception {
                Web3j w3 = network.value.w3;
                BigInteger fromWalletBalance = w3
                                .ethGetBalance(fromWallet, DefaultBlockParameterName.LATEST).send().getBalance();
                if (amount.compareTo(fromWalletBalance) != -1) {
                        // TODO - Wallet ETH Balance Must be more than desired amount
                        throw new Exception();
                }
                System.out.println(String.format("Current Wallet %s Balance is %s", fromWallet, fromWalletBalance));
                GasStation gasStation = new GasStation(network);
                BigInteger gasPrice = gasStation.gasPrice;
                BigInteger blockNumber = w3.ethBlockNumber().send().getBlockNumber();
                BigInteger nonce = w3.ethGetTransactionCount(
                                fromWallet, DefaultBlockParameterName.LATEST).send().getTransactionCount();

                RawTransaction transaction = RawTransaction
                                .createEtherTransaction(
                                                nonce,
                                                gasPrice,
                                                currentGasLimit,
                                                toWallet,
                                                amount);
                String trx_obj = String.format(
                                "Generated transferETH Trx :{'value':%s,'data':'%s','from':'%s'" +
                                                ",'to':'%s','nonce':%s,'gasPrice':%s,'gas':%s,'requestedBlockNumber':}",
                                transaction.getValue(), transaction.getData(), fromWallet, transaction.getTo(),
                                transaction.getNonce(), transaction.getGasPrice(), transaction.getGasLimit(),
                                blockNumber.toString()

                );
                network.logger.info(trx_obj);

                return new RawTransactionAndExtraInfo(transaction, blockNumber, network, gasStation);

        }

        /**
         * Generates simple Raw Transaction object for Transfer-ing ERC20 from one
         * address to
         * another
         * 
         * @param network
         * @param token
         * @param fromWallet
         * @param toWallet
         * @param amount
         * @param currentGasLimit
         * @return RawTransactionAndExtraInfo
         * @throws Exception
         */
        public static RawTransactionAndExtraInfo transferToken(Network network, Token token, String fromWallet,
                        String toWallet,
                        BigInteger amount, BigInteger currentGasLimit)
                        throws Exception {
                Web3j w3 = network.value.w3;
                BigInteger fromWalletBalance = token.tokenContract().balanceOf(fromWallet).send();
                if (amount.compareTo(fromWalletBalance) != -1) {
                        // TODO - Wallet ETH Balance Must be more than desired amount
                        throw new Exception();
                }
                System.out.println(String.format("Current Wallet %s Balance is %s",
                                fromWallet, fromWalletBalance));
                GasStation gasStation = new GasStation(network);
                BigInteger gasPrice = gasStation.gasPrice;
                BigInteger blockNumber = w3.ethBlockNumber().send().getBlockNumber();
                BigInteger nonce = w3.ethGetTransactionCount(
                                fromWallet, DefaultBlockParameterName.LATEST).send().getTransactionCount();
                String data = transferTrxDataGenerator(ERC20TRANSFER_FUNCTION_SELECTOR, fromWallet, amount);

                RawTransaction transaction = RawTransaction
                                .createTransaction(
                                                nonce,
                                                gasPrice,
                                                currentGasLimit,
                                                toWallet,
                                                BigInteger.ZERO,
                                                data);
                String trx_obj = String.format(
                                "Generated transferToken Trx :{'value':%s,'data':'%s','from':'%s'" +
                                                ",'to':'%s','nonce':%s,'gasPrice':%s,'gas':%s,'requestedBlockNumber':}",
                                transaction.getValue(), transaction.getData(), fromWallet, transaction.getTo(),
                                transaction.getNonce(), transaction.getGasPrice(), transaction.getGasLimit(),
                                blockNumber.toString()

                );
                network.logger.info(trx_obj);

                return new RawTransactionAndExtraInfo(transaction, blockNumber, network, gasStation);

        }

}
