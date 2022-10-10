package com.core.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionObject;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;

import com.core.models.TrxReceipt;
import com.core.models.block.ScannedBlocks;

/*
* Used Via Worker to reads blocks one by one 
* fills TRXReceipt
* finds deposit  
* accepts withdrawal requests  
*/
public class BlockScanner {

    public static Map<Long, List<TrxReceipt>> fetchBlockNumbers(Network network, Long fromblockNumber,
            Long toblockNumber)
            throws IOException {
        Map<Long, List<TrxReceipt>> scannedBlocks = new HashMap<>();
        for (long i = fromblockNumber + 1; i <= toblockNumber; i++) {
            try {

                System.out.println(String.format("Scanning %d", i));
                scannedBlocks.put(i, fetchBlockNumber(network, i));
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }
        }
        return scannedBlocks;
    }

    public static List<TrxReceipt> fetchBlockNumber(Network network, Long blockNumber) throws IOException {
        Web3j w3 = network.value.w3;
        DefaultBlockParameterNumber blockNumberObj = new DefaultBlockParameterNumber(blockNumber);
        Block block = w3.ethGetBlockByNumber(blockNumberObj, true).send().getBlock();
        ArrayList<TrxReceipt> trxReceipts = new ArrayList<>();
        for (TransactionResult<TransactionObject> trx : block.getTransactions()) {
            trxReceipts.add(TrxReceipt.fromTransactionObject(trx.get()));
        }
        return trxReceipts;
    }

}
