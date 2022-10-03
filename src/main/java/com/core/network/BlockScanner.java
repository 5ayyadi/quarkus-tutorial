package com.core.network;

import java.io.IOException;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock.Block;

import com.core.models.block.ScannedBlocks;

/*
* Used Via Worker to reads blocks one by one 
* fills TRXReceipt
* finds deposit  
* accepts withdrawal requests  
*/
public class BlockScanner {

    // ScannedBlocks scannedBlock;
    // Network network;
    // public BlockScanner(Long blocknumber) {
    // }

    public static ScannedBlocks fetchBlockNumber(Network network, Long blockNumber) throws IOException {
        Web3j w3 = network.value.w3;
        DefaultBlockParameterNumber blockNumberObj = new DefaultBlockParameterNumber(blockNumber);
        Block b = w3.ethGetBlockByNumber(blockNumberObj, true).send().getBlock();
        ScannedBlocks sb = new ScannedBlocks(blockNumber, network);
        return sb;
        // System.out.println(b);
    }

}
