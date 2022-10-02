package com.core.models;

import java.math.BigInteger;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Table(name = "TrxReceipt")
@Entity
public class TrxReceipt extends PanacheEntityWithTime {

    public String transactionHash;
    public Long transactionIndex;
    public String blockHash;
    public Long blockNumber;
    public BigInteger cumulativeGasUsed;
    public BigInteger gasUsed;
    public String contractAddress;
    public String root;
    // status is only present on Byzantium transactions onwards
    // see EIP 658 https://github.com/ethereum/EIPs/pull/658
    public String status;
    public String fromAddress;
    public String toAddress;
    public String logsBloom;
    public String data;
    public BigInteger requestedBlockNumber;

    public TrxReceipt() {
    }

    public TrxReceipt(String transactionHash, Long transactionIndex,
            String blockHash, Long blockNumber, BigInteger cumulativeGasUsed,
            BigInteger gasUsed, String contractAddress, String root, String status,
            String from, String to, String logsBloom, String data) {
        this.transactionHash = transactionHash;
        this.transactionIndex = transactionIndex;
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
        this.cumulativeGasUsed = cumulativeGasUsed;
        this.gasUsed = gasUsed;
        this.contractAddress = contractAddress;
        this.root = root;
        this.status = status;
        this.fromAddress = from;
        this.toAddress = to;
        this.logsBloom = logsBloom;
        this.data = data;
    }

    public static TrxReceipt fromTransaction(Optional<TransactionReceipt> trx) {
        if (trx != null) {
            return TrxReceipt.fromTransaction(trx.get());
        }
        return null;

    }

    public static TrxReceipt fromTransaction(TransactionReceipt trx) {
        // TODO - Transaction Data
        return new TrxReceipt(
                trx.getTransactionHash(),
                trx.getTransactionIndex().longValue(),
                trx.getBlockHash(),
                trx.getBlockNumber().longValue(),
                trx.getCumulativeGasUsed(),
                trx.getGasUsed(),
                trx.getContractAddress(),
                trx.getRoot(),
                trx.getStatus(),
                trx.getFrom(),
                trx.getTo(),
                trx.getLogsBloom(),
                "0x");
    }

}
