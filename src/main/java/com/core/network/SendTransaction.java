package com.core.network;

import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthChainId;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;
import org.web3j.utils.Bytes;
import com.core.models.TrxReceipt;
import com.core.models.wallet.Wallet;
import com.core.network.TransactionGeneration.RawTransactionAndExtraInfo;

public class SendTransaction {

    public static TrxReceipt SignAndSend(
            RawTransactionAndExtraInfo trx,
            Wallet wallet)
            throws Exception {
        return SignAndSend(trx.network, trx.rawTransaction, wallet.getPrivateKey(), wallet.getPublicKey());
    }

    public static TrxReceipt SignAndSend(
            RawTransactionAndExtraInfo trx,
            String privateKey,
            String publicKey)
            throws Exception {
        return SignAndSend(trx.network, trx.rawTransaction, privateKey, publicKey);
    }

    public static TrxReceipt SignAndSend(
            Network network,
            RawTransaction rawTransaction,
            Wallet wallet)
            throws Exception {
        return SignAndSend(network, rawTransaction, wallet.getPrivateKey(), wallet.getPublicKey());
    }

    public static TrxReceipt SignAndSend(
            Network network,
            RawTransaction rawTransaction,
            String privateKey,
            String publicKey)
            throws Exception {
        BigInteger requestedBlockNumber = network.w3.ethBlockNumber().send().getBlockNumber();
        return SignAndSend(network, rawTransaction, privateKey, publicKey, requestedBlockNumber);
    }

    public static TrxReceipt SignAndSend(
            Network network,
            RawTransaction rawTransaction,
            String privateKey,
            String publicKey,
            BigInteger requestedBlockNumber)
            throws Exception {
        Web3j w3 = network.value.w3;
        Credentials credentials = Credentials.create(privateKey);
        if (!credentials.getAddress().equals(("0x" + publicKey).toLowerCase())) {
            // TODO - miss match privatekey wallet address
            throw new Exception();
        }
        // byte chainIdByteR = Numeric.asByte(4002, 0);
        // byte chainIdByte = network.value.chainId.byteValue();
        EthChainId chainId =  network.w3.ethChainId().send();
        byte[] signedMessage = TransactionEncoder. signMessage(
                rawTransaction,
                chainId.getId(),
                credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = w3.ethSendRawTransaction(hexValue).send();
        System.out.println(ethSendTransaction.getError().getMessage());

        String trxHash = ethSendTransaction.getResult();
        EthGetTransactionReceipt trx = w3.ethGetTransactionReceipt(trxHash).send();
        return TrxReceipt.fromTransaction(trx.getResult());

    }
}
