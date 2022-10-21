package com.core.schemas.request;

import java.math.BigInteger;

public class PcmTransferRequest extends MasterWalletRequest {
    public String toWallet;
    public BigInteger amount;
    public String tokenAddress;

}
