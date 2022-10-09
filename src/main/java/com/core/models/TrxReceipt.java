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

    @Column(unique = true)
    public String transactionHash;

    @Column(columnDefinition = "text", nullable = true)
    public Long transactionIndex;

    // TODO - make this one field ( just a fk to Scannedblocks)
    // @ManyToOne
    // public ScannedBlocks blockNumberId;
    @Column(columnDefinition = "text", nullable = true)
    public Long blockNumber;
    @Column(columnDefinition = "text", nullable = true)
    public String blockHash;
    // END-TODO

    @Column(nullable = true)
    public BigInteger cumulativeGasUsed;
    @Column(nullable = true)
    public BigInteger gasUsed;

    // public BigInteger gasPriceInDollar; TODO - GAS-Station

    public String fromAddress;
    public String toAddress;

    @Column(nullable = true, length = 65535, columnDefinition = "text")
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
        this.status = status;
        this.fromAddress = from;
        this.toAddress = to;
        this.data = data;
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
                trx.getStatus(),
                trx.getFrom(),
                trx.getTo(),
                // trx.
                null);
    }

    @Override
    public String toString() {
        return "TrxReceipt [transactionHash=" + transactionHash + ", transactionIndex=" + transactionIndex
                + ", blockNumber=" + blockNumber + ", blockHash=" + blockHash
                + ", cumulativeGasUsed=" + cumulativeGasUsed + ", gasUsed=" + gasUsed
                + ", fromAddress=" + fromAddress + ", toAddress=" + toAddress
                + ", data=" + data + ", status=" + status + ", requestedBlockNumber=" + requestedBlockNumber
                + "]";
    }

}
