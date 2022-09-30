package test;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.utils.Numeric;

import com.core.network.Network;
import com.core.network.TransactionGeneration;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class A<T, G> {
    public A(T a, G b) {

    }
}

public class main {

    private static Web3j web3j;

    public static void main(String[] args) {
        web3j = Web3j.build(new HttpService("http://localhost:8545/"));

        Web3ClientVersion web3ClientVersion = null;
        try {
            String fromWallet = "0xcd3B766CCDd6AE721141F452C550Ca635964ce71";
            RawTransaction rawTransaction = TransactionGeneration.sendETH(Network.EthereumLocal,
                    fromWallet,
                    "0x2546BcD3c84621e976D8185a91A922aE77ECEc30",
                    new BigInteger("999999914922203320700"));
            // Web3j w3 =
            String trx_obj = String.format(
                    "{'value':%s,'data':'%s','from':'%s' ,'to':'%s','nonce':%s,'gasPrice':%s,'gas':%s}",
                    rawTransaction.getValue(),
                    rawTransaction.getData(),
                    fromWallet,
                    rawTransaction.getTo(),
                    rawTransaction.getNonce(),
                    rawTransaction.getGasPrice(),
                    rawTransaction.getGasLimit()

            );
            System.out.println(trx_obj);

            Credentials credentials = Credentials
                    .create("8166f546bab6da521a8369cab06c5d2b9e46670292d85c875ee9ec20e84ffb61");
            // loadCredentials("password", "/path/to/walletfile");

            // sign & send our transaction
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
            String trxHash = ethSendTransaction.getResult();
            Optional<TransactionReceipt> trx = web3j.ethGetTransactionReceipt(trxHash).send().getTransactionReceipt();
            System.out.println(trx);

        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
    }
}
