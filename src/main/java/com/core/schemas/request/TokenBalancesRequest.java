package com.core.schemas.request;

import com.core.math.Decimal;

public class TokenBalancesRequest {
    public String tokenAddress;
    public String walletAddress;
    public Decimal balance;
}
