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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.json.Json;
import javax.json.stream.JsonParser;

public class main {

    public static void main(String[] args) {
        Web3j w3 = Network.EthereumLocal.value.w3;

        try {
            // String j = "{\"wbnb\":{\"usd\":278.91}}";
            // ObjectMapper mapper = new ObjectMapper();
            // JsonNode jt = mapper.readTree(j);
            // System.out.println(jt.get("wbnb").get("usd"));

            // DefaultBlockParameterNumber blockNumberObj = new
            // DefaultBlockParameterNumber(w3.get);
            Block block = w3.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, true).send().getBlock();
            System.out.println(block.getTimestampRaw());
            System.out.println(block.getTimestamp());
            System.out.println(block.getAuthor());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
