package com.core.schemas.request;

import java.math.BigInteger;

import com.core.models.TransactionStatus;

public class TransferRequest {
    public Long fromUID;
    public Long toUID;
    public Long tokenId;
    public BigInteger amount;
    public TransactionStatus status = TransactionStatus.RECEIVED;

    public void changeStatus(TransactionStatus status){
        this.status = status;
    }
}
