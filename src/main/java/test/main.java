package test;

import java.math.BigInteger;

import com.core.models.TrxReceipt;
import com.core.network.Network;
import com.core.network.SendTransaction;
import com.core.network.TransactionGeneration;
import com.core.network.TransactionGeneration.RawTransactionAndExtraInfo;

public class main {

    public static void main(String[] args) {

        try {
            Network network = Network.FtmTestnet;
            String fromWallet = "0x5638f545C240E52920F49C035BA6e85846d229D6";
            String privKey = "01aa1bde721c4f3f88ffaff51b148dd32a8110450af9021c1c1777f38b0c0ead";
            String pubKey = "5638f545c240e52920f49c035ba6e85846d229d6";
            String toWallet = "0x4c97380af08e1ee1846f00f737c0e1121087fedd";
            BigInteger amount = new BigInteger("100000000");

            RawTransactionAndExtraInfo trx = TransactionGeneration.transferETH(network, fromWallet, toWallet, amount);
            System.out.println(trx.rawTransaction);
            TrxReceipt receipt = SendTransaction.SignAndSend(trx, privKey, pubKey);
            System.out.println(receipt);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
