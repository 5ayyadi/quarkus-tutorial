package com.core.models;

import java.math.BigInteger;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionObject;

import com.core.models.block.ScannedBlocks;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Table(name = "TrxReceipt")
@Entity
public class TrxReceipt extends PanacheEntityWithTime {

    @Column(columnDefinition = "text", nullable = true)
    public String transactionHash;

    @Column(columnDefinition = "text", nullable = true)
    public Long transactionIndex;

    // @Column(nullable = true)
    // TODO - make this one field ( just a fk to Scannedblocks)
    // @ManyToOne
    // public ScannedBlocks blockNumberId;
    @Column(columnDefinition = "text", nullable = true)
    public Long blockNumber;
    @Column(columnDefinition = "text", nullable = true)
    public String blockHash;
    // END-TODO

    // @Column(nullable = true)
    @Column(columnDefinition = "text", nullable = true)
    public BigInteger cumulativeGasUsed;
    @Column(columnDefinition = "text", nullable = true)
    public BigInteger gasUsed;
    @Column(columnDefinition = "text", nullable = true)
    public String contractAddress;
    // public String root;
    // status is only present on Byzantium transactions onwards
    // see EIP 658 https://github.com/ethereum/EIPs/pull/658

    @Column(columnDefinition = "text", nullable = true)
    public String fromAddress;
    @Column(columnDefinition = "text", nullable = true)
    public String toAddress;
    // public String logsBloom;
    @Column(nullable = true, length = 65535, columnDefinition = "Text")
    @Type(type = "text")
    public String data;
    // Should Later be field
    @Column(columnDefinition = "text", nullable = true)
    public String status;
    // NOTE - is this field really needed ?

    @Column(nullable = true)
    public BigInteger requestedBlockNumber;

    public TrxReceipt() {
    }

    public TrxReceipt(String transactionHash, Long transactionIndex,
            String blockHash, Long blockNumber, BigInteger cumulativeGasUsed,
            BigInteger gasUsed, String status,
            String from, String to, String data) {
        this.transactionHash = transactionHash;
        this.transactionIndex = transactionIndex;
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
        this.cumulativeGasUsed = cumulativeGasUsed;
        this.gasUsed = gasUsed;
        // this.contractAddress = contractAddress;
        // this.root = root;
        this.status = status;
        this.fromAddress = from;
        this.toAddress = to;
        // this.logsBloom = logsBloom;
        this.data = data;
    }

    public static TrxReceipt fromTransaction(Optional<TransactionReceipt> trx) {
        if (trx != null) {
            return TrxReceipt.fromTransaction(trx.get());
        }
        return null;
    }

    public static TrxReceipt fromTransactionObject(TransactionObject trx) {
        return new TrxReceipt(
                trx.getHash(),
                trx.getTransactionIndex().longValue(),
                trx.getBlockHash(),
                trx.getBlockNumber().longValue(),
                BigInteger.ZERO,
                trx.getGas(),
                "FOUND", // TODO make this use enum
                trx.getFrom(),
                trx.getTo(),
                trx.getInput());

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
                // trx.getContractAddress(),
                // trx.getRoot(),
                trx.getStatus(),
                trx.getFrom(),
                trx.getTo(),
                // trx.getLogsBloom(),
                null);
    }

    @Override
    public String toString() {
        return "TrxReceipt [transactionHash=" + transactionHash + ", transactionIndex=" + transactionIndex
                + ", blockNumber=" + blockNumber + ", blockHash=" + blockHash
                + ", cumulativeGasUsed=" + cumulativeGasUsed + ", gasUsed=" + gasUsed + ", contractAddress="
                + contractAddress + ", fromAddress=" + fromAddress + ", toAddress=" + toAddress
                + ", data=" + data + ", status=" + status + ", requestedBlockNumber=" + requestedBlockNumber
                + "]";
    }

}
