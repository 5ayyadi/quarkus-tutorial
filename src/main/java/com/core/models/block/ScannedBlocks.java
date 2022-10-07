package com.core.models.block;

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

import com.core.models.PanacheEntityWithTime;
import com.core.models.TrxReceipt;
import com.core.network.Network;

import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;

@Entity
public class ScannedBlocks extends PanacheEntityWithTime {

    // @Id
    @Column(name = "blockNumber")
    Long blockNumber;
    Network network;
    BlockScanStatus scanStatus;

    @OneToMany
    @JoinColumn(name = "TrxReceipt_id")
    Set<TrxReceipt> trxs = new HashSet<TrxReceipt>();

    @Transient
    public static final long INITIAL_BLOCK_NUMBER = 15259989;

    public ScannedBlocks() {
    }

    public ScannedBlocks(
            Long blockNumber,
            Network network,
            BlockScanStatus scanStatus) {
        this.blockNumber = blockNumber;
        this.network = network;
        if (scanStatus == null) {
            this.scanStatus = BlockScanStatus.PENDING;
        } else {
            this.scanStatus = scanStatus;
        }
    }

    public ScannedBlocks(Long blockNumber, Network network) {
        this(blockNumber, network, null);
    }

    public static Long lastScannedBlock() {
        Sort sort = Sort.by("blockNumber", Direction.Descending, null);

        ScannedBlocks scannedBlock = (ScannedBlocks) findAll(sort).firstResult();
        // ScannedBlocks scannedBlock = find("blockNumber DESC").firstResult();
        if (scannedBlock != null) {

            return scannedBlock.blockNumber;
        }
        return (long) INITIAL_BLOCK_NUMBER;
    }
}
