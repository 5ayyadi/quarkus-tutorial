package com.core.network;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;

import com.core.models.TrxReceipt;

public class SendTransaction {
    public static TrxReceipt SignAndSend(
            Network network,
            RawTransaction rawTransaction,
            String privateKey,
            String publicKey,
            BigInteger requestedBlockNumber)
            throws Exception {
        Web3j w3 = network.value.w3;
        Credentials credentials = Credentials.create(privateKey);
        if (credentials.getAddress() != publicKey) {
            // TODO - miss match privatekey wallet address
            throw new Exception();
        }

        // String trx_obj = String.format(
        // "{'value':%s,'data':'%s','from':'%s'
        // ,'to':'%s','nonce':%s,'gasPrice':%s,'gas':%s}",
        // rawTransaction.getValue(),
        // rawTransaction.getData(),
        // publicKey,
        // rawTransaction.getTo(),
        // rawTransaction.getNonce(),
        // rawTransaction.getGasPrice(),
        // rawTransaction.getGasLimit()

        // );
        // System.out.println(trx_obj);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = w3.ethSendRawTransaction(hexValue).send();
        String trxHash = ethSendTransaction.getResult();
        Optional<TransactionReceipt> trx = w3.ethGetTransactionReceipt(trxHash).send().getTransactionReceipt();

        return TrxReceipt.fromTransaction(trx.get());

    }
}
