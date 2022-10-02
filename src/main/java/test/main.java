package test;

import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.utils.Numeric;
import org.web3j.utils.Strings;

import com.core.network.Network;
import com.core.network.TransactionGeneration;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class main {

    public static void main(String[] args) {
        Web3j w3 = Network.EthereumLocal.value.w3;

        try {
            // TODO - ADD block scanner
            DefaultBlockParameterNumber blockNumber = new DefaultBlockParameterNumber(15260005);
            Block b = w3.ethGetBlockByNumber(blockNumber, true).send().getBlock();
            // System.out.println(b);
            // System.out.println(b.getTransactions());
            Date d = new Date();
            System.out.println(d);
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
