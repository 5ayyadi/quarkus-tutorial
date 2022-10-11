package com.core.schemas.request;

import java.math.BigInteger;

import com.core.models.TransactionStatus;
import com.core.network.Network;

public class WithdrawDepositRequest extends TokenBalancesRequest {
    public Long userId;
    public Long tokenId;
    public TransactionStatus status = TransactionStatus.RECEIVED;
    public String trxHash;

    public WithdrawDepositRequest() {
    }

    public WithdrawDepositRequest(Long userId, Long tokenId, String walletAddress, String tokenAddress, BigInteger amount,
            Network network, String trxHash) {
        this.userId = userId;
        this.tokenId = tokenId;
        this.walletAddress = walletAddress;
        this.tokenAddress = tokenAddress;
        this.amount = amount;
        this.network = network;
        this.trxHash = trxHash;
    }

    public void changeStatus(TransactionStatus status) {
        this.status = status;
    }
}
