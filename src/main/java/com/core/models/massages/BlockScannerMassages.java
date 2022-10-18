package com.core.models.massages;

import com.core.models.block.ScannedBlocks;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class BlockScannerMassages {

    public String id;
    public ScannedBlocks block;

    public String Status;

    public BlockScannerMassages(String id, ScannedBlocks block, String status) {
        this.id = id;
        this.block = block;
        Status = status;
    }

    /**
     * Default constructor required for Jackson serializer
     */
    public BlockScannerMassages() {
    }

    @Override
    public String toString() {
        return "BlockScannerMassages [id=" + id + ", block=" + block + ", Status=" + Status + "]";
    }

}
