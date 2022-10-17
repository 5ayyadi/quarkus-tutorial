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
public class TrxReceipt extends PanacheEntityBaseWithTime {

    @Column(unique = true)
    @Id
    public String transactionHash;

    @Column(columnDefinition = "text", nullable = true)
    public Long transactionIndex;

    @ManyToOne
    public ScannedBlocks scannedBlock;

    @Column(nullable = true)
    public BigInteger cumulativeGasUsed;
    @Column(nullable = true)
    public BigInteger gasUsed;

    @Column(nullable = true)
    public Double nativeTokenPrice;

    public String fromAddress;
    public String toAddress;

    @Column(nullable = true, length = 65535, columnDefinition = "text")
    @Type(type = "text")
    public String data;

    public TransactionStatus status;
    public TransactionType trxType;

    @Column(precision = 100, scale = 0)
    public BigInteger amount;

    public boolean isErc20 = false;

    // TODO ..... parse transaction data
    @Column(nullable = true)
    private String ERC20ReceiverAddress;

    public TrxReceipt() {
    }

    public TrxReceipt(String transactionHash, Long transactionIndex,
            BigInteger cumulativeGasUsed,
            BigInteger gasUsed, TransactionStatus status,
            String from, String to, String data, ScannedBlocks scannedBlocks, Double nativeTokenPrice,
            BigInteger amount, Address ERC20ReceiverAddress) {
        this.transactionHash = transactionHash;
        this.transactionIndex = transactionIndex;
        this.scannedBlock = scannedBlocks;
        this.nativeTokenPrice = nativeTokenPrice;
        this.cumulativeGasUsed = cumulativeGasUsed;
        this.gasUsed = gasUsed;
        this.status = status;
        this.fromAddress = from;
        this.toAddress = to;
        this.data = data;
        this.amount = amount;
        if (ERC20ReceiverAddress != null) {
            this.ERC20ReceiverAddress = ERC20ReceiverAddress.toString();
            this.isErc20 = true;
        } else
            this.ERC20ReceiverAddress = null;
    }

    public static TrxReceipt fromTransaction(TransactionReceipt trx) {
        // TODO - Transaction Data
        TransactionStatus trxStatus;
        if (trx.getStatus() == "1") {
            trxStatus = TransactionStatus.FOUND;
        } else if (trx.getStatus() == "0") {
            trxStatus = TransactionStatus.FAILED;
        } else {
            trxStatus = TransactionStatus.UNKNOWN;

        }
        return new TrxReceipt(
                trx.getTransactionHash(),
                trx.getTransactionIndex().longValue(),
                // trx.getBlockHash(),
                // trx.getBlockNumber().longValue(),
                trx.getCumulativeGasUsed(),
                trx.getGasUsed(),
                trxStatus,
                trx.getFrom(),
                trx.getTo(),
                null, null, null, null, null);
    }

    public static TrxReceipt castTransactionObjectToTrxReceipt(TransactionObject trx, ScannedBlocks scannedBlock,
            Double nativeTokenPrice) {

        BigInteger amount = TrxReceipt.getERC20Amount(trx.getInput());
        if (amount == null)
            amount = trx.getValue();
        Address receiver_address;
        try {
            receiver_address = (new TransactionDecoder.ERC20Transfer(trx.getInput())).fromAddress;
        } catch (Exception e) {
            receiver_address = null;
        }
        return new TrxReceipt(
                trx.getHash(),
                trx.getTransactionIndex().longValue(),
                BigInteger.ZERO,
                trx.getGas(),
                TransactionStatus.FOUND, // TODO make this use enum
                trx.getFrom(),
                trx.getTo(),
                trx.getInput(),
                scannedBlock,
                nativeTokenPrice,
                amount,
                receiver_address);

    }

    public Address getERC20ReceiverAddress() {
        if (ERC20ReceiverAddress == null) {
            return null;
        } else
            return new Address(ERC20ReceiverAddress);
    }

    public static BigInteger getERC20Amount(String data) {
        try {
            return (new TransactionDecoder.ERC20Transfer(data)).amount;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    @Override
    public String toString() {
        return "TrxReceipt [transactionHash=" + transactionHash + ", transactionIndex=" + transactionIndex
                + ", scannedBlock=" + scannedBlock + ", cumulativeGasUsed=" + cumulativeGasUsed + ", gasUsed=" + gasUsed
                + ", nativeTokenPrice=" + nativeTokenPrice + ", fromAddress=" + fromAddress + ", toAddress=" + toAddress
                + ", data=" + data + ", status=" + status + ", trxType=" + trxType + ", amount=" + amount + ", isErc20="
                + isErc20 + ", ERC20ReceiverAddress=" + ERC20ReceiverAddress + "]";
    }

}
