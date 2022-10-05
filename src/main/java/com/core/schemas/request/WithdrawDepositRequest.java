package com.core.schemas.request;

import java.math.BigInteger;

import com.core.models.TransactionStatus;
import com.core.network.Network;

public class WithdrawDepositRequest {
    public Long userId;
    public String walletAddress;
    public String tokenAddress;
    public BigInteger amount;
    public Network network = Network.BSC;
    public TransactionStatus status = TransactionStatus.RECEIVED;
    public String trxHash;

}
