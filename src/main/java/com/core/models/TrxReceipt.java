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
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionObject;

import com.core.models.block.ScannedBlocks;
import com.core.network.TransactionDecoder;
import com.core.network.TransactionGeneration;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.logging.Log;

@Table(name = "TrxReceipt")
@Entity
public class TrxReceipt extends PanacheEntityWithTime {

    @Column(unique = true)
    public String transactionHash;

    @Column(columnDefinition = "text", nullable = true)
    public Long transactionIndex;

    // TODO - make this one field ( just a fk to Scannedblocks)
    @ManyToOne
    public ScannedBlocks blockNumberId;
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

    public TrxReceiptStatus status;
    public TransactionType trxType;
    // NOTE - is this field really needed ?

    @Column(nullable = true)
    public BigInteger requestedBlockNumber;

    // TODO ..... parse transaction data
    @Column(nullable = true)
    public String ERC20ReceiverAddress;

    public TrxReceipt() {
    }

    public TrxReceipt(String transactionHash, Long transactionIndex,
            String blockHash, Long blockNumber, BigInteger cumulativeGasUsed,
            BigInteger gasUsed, TrxReceiptStatus status,
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
                TrxReceiptStatus.FOUND, // TODO make this use enum
                trx.getFrom(),
                trx.getTo(),
                trx.getInput());

    }

    public static TrxReceipt fromTransaction(TransactionReceipt trx) {
        // TODO - Transaction Data
        TrxReceiptStatus trxStatus;
        if (trx.getStatus() == "1") {
            trxStatus = TrxReceiptStatus.FOUND;
        } else if (trx.getStatus() == "0") {
            trxStatus = TrxReceiptStatus.FAILED;
        } else {
            trxStatus = TrxReceiptStatus.UNKNOWN;

        }
        return new TrxReceipt(
                trx.getTransactionHash(),
                trx.getTransactionIndex().longValue(),
                trx.getBlockHash(),
                trx.getBlockNumber().longValue(),
                trx.getCumulativeGasUsed(),
                trx.getGasUsed(),
                trxStatus,
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

    public Optional<Address> getERC20ReceiverAddress() {
        // Optional<String> res = Optional.ofNullable(null);
        try {

            // TransactionDecoder.ERC20Transfer x = new
            // TransactionDecoder.ERC20Transfer(this.data);
            return Optional.of((new TransactionDecoder.ERC20Transfer(this.data)).fromAddress);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            // Log.errorf("trxHash:%s , trxData:%s", this.transactionHash, this.data);
            return Optional.ofNullable(null);
        }

    }

}
