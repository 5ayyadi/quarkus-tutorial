package test;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

            // new A<String, String>("asd", 10) ;

            // // ERC
            // web3ClientVersion = web3j.web3ClientVersion().send();
            // String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            // System.out.println("clientVersion " + clientVersion);
            // List<String> rpcList = ArrayList.asList("asd");
            List<String> places = Arrays.asList("Buenos Aires", "Córdoba", "La Plata");

            BigInteger bigInteger = new BigInteger("18");
            bigInteger.intValue();
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
    }
}
