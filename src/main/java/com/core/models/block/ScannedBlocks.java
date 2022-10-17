package com.core.models.block;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import com.core.models.PanacheEntityBaseWithTime;
import com.core.models.PanacheEntityWithTime;
import com.core.models.TrxReceipt;
import com.core.network.Network;

import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import io.quarkus.runtime.annotations.RegisterForReflection;

@Entity
@RegisterForReflection
public class ScannedBlocks extends PanacheEntityBaseWithTime {

    @Column(name = "blockNumber", unique = true)
    @Id
    Long blockNumber;

    public Network network;
    public Timestamp blockTimestamp;
    public BlockScanStatus scanStatus;

    int trxCount;

    @OneToMany
    @JoinColumn(name = "TrxReceipt_id")
    Set<TrxReceipt> trxs = new HashSet<TrxReceipt>();

    @Transient
    // public static final long INITIAL_BLOCK_NUMBER = 15259989; //NOTE - LOCAL -
    // ETH
    public static final long INITIAL_BLOCK_NUMBER = 11196262L; // NOTE - TESTNET - FTM

    public ScannedBlocks() {
    }

    public ScannedBlocks(
            Long blockNumber,
            Network network,
            BlockScanStatus scanStatus,
            int trxCount,
            Timestamp timestamp) {
        this.blockNumber = blockNumber;
        this.network = network;
        this.trxCount = trxCount;
        this.blockTimestamp = timestamp;
        if (scanStatus == null) {
            this.scanStatus = BlockScanStatus.PENDING;
        } else {
            this.scanStatus = scanStatus;
        }
    }

    public Set<TrxReceipt> getTrxs() {
        return trxs;
    }

    public static Long lastScannedBlock() {
        Sort sort = Sort.by("blockNumber", Direction.Descending, null);

        ScannedBlocks scannedBlock = (ScannedBlocks) findAll(sort).firstResult();
        if (scannedBlock != null) {
            return scannedBlock.blockNumber;
        }
        return (long) INITIAL_BLOCK_NUMBER;
    }

    @Override
    public String toString() {
        return "ScannedBlocks [blockNumber=" + blockNumber + ", network=" + network + ", scanStatus=" + scanStatus
                + ", trxs=" + trxs + "]";
    }

}
