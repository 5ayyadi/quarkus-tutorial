package com.core.schemas.request;

import java.math.BigInteger;

import com.core.models.Token;
import com.core.models.TransactionStatus;
import com.core.models.wallet.*;;

public class WithdrawDepositRequest {
    public Wallet source;
    public Wallet destination;
    public Token token;
    public BigInteger amount;
    public TransactionStatus status = TransactionStatus.RECEIVED;

}
