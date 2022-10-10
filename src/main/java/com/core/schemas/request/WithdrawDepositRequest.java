package com.core.schemas.request;


import com.core.models.TransactionStatus;

public class WithdrawDepositRequest extends TokenBalancesRequest{
    public Long userId;
    public Long tokenId;
    public TransactionStatus status = TransactionStatus.RECEIVED;
    public String trxHash;

    public void changeStatus(TransactionStatus status) {
        this.status = status;
    }
}
