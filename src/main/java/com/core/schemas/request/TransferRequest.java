package com.core.schemas.request;

import java.math.BigInteger;

import com.core.models.TransactionStatus;
import com.core.models.wallet.WalletTransactionType;

public class TransferRequest {
    public Long fromUID;
    public Long toUID;
    public Long tokenId;
    public BigInteger amount;
    public WalletTransactionType type = WalletTransactionType.TRANSFER;
    public TransactionStatus status = TransactionStatus.RECEIVED;

    public void changeStatus(TransactionStatus status){
        this.status = status;
    }
}
