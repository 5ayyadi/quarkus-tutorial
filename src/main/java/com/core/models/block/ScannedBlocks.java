package com.core.models.block;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.core.models.PanacheEntityWithTime;
import com.core.network.Network;

enum ScanStatus {
    SUCCESS,
    FAILED,
    PENDING,
    IN_PROGRESS,
    DONE;
}

@Entity
public class ScannedBlocks extends PanacheEntityWithTime {

    // @Id
    Long blockNumber;
    Network network;
    ScanStatus scanStatus;

}
