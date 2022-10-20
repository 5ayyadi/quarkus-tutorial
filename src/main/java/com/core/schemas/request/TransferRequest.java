package com.core.schemas.request;

import java.math.BigInteger;

import com.core.models.TransactionStatus;
import com.core.models.TransactionType;

public class TransferRequest {
    public Long fromUID;
    public Long toUID;
    public Long tokenId;
    public BigInteger amount;
    public TransactionType type = TransactionType.TRANSFER;
    public TransactionStatus status = TransactionStatus.RECEIVED;

    
    public TransferRequest(Long fromUID, Long toUID, Long tokenId, BigInteger amount, TransactionType type,
            TransactionStatus status) {
        this.fromUID = fromUID;
        this.toUID = toUID;
        this.tokenId = tokenId;
        this.amount = amount;
        this.type = type;
        this.status = status;
    }


    public void changeStatus(TransactionStatus status) {
        this.status = status;
    }
}
