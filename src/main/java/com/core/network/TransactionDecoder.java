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

public class TransactionDecoder {

        private static final BigInteger DEFAULT_GAS_LIMIT = new BigInteger("30000000");
        private static final String TRANSFER_FUNCTION_SELECTOR = "0xa9059cbb";
        private static final BigInteger MAX_ALLOWED_INT = new BigInteger(
                        "115792089237316195423570985008687907853269984665640564039457584007913129639935");

        // private static final String SENT_ETH_DATA = ;
        // 0000000000000000000000008ac76a51cc950d9822d68b83fe1ad97b32cd580d0000000000000000000000000000000000000000000000000000000000000096;
        public static class ERC20Transfer {
                public Address fromAddress;
                public BigInteger amount;

                public ERC20Transfer(String trxData) throws Exception {
                        super();
                        if (trxData.startsWith(TRANSFER_FUNCTION_SELECTOR)) {
                                int fns_l = TRANSFER_FUNCTION_SELECTOR.length();
                                if (trxData.length() == (fns_l + (2 * 64))) {
                                        this.fromAddress = new Address(
                                                        new BigInteger(trxData.substring(fns_l, fns_l + 64), 16));
                                        this.amount = new BigInteger(trxData.substring(fns_l + 64, fns_l + (2 * 64)),
                                                        16);
                                }
                        } else {
                                throw new Exception();
                        }
                }
        }

}
