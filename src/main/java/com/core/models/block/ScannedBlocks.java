package com.core.models.block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.core.models.PanacheEntityWithTime;
import com.core.models.TrxReceipt;
import com.core.network.Network;

@Entity
public class ScannedBlocks extends PanacheEntityWithTime {

    // @Id
    Long blockNumber;
    Network network;
    BlockScanStatus scanStatus;

    @OneToMany
    @JoinColumn(name = "TrxReceipt_id")
    Set<TrxReceipt> trxs = new HashSet<TrxReceipt>();

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

}
