package com.core.schemas.trxDecoder;

import java.math.BigInteger;

import org.web3j.protocol.core.methods.response.Transaction;

public class TransferDecoder {
    public String address;
    public BigInteger amount;

    public TransferDecoder(String address, BigInteger amount) {
        this.address = address;
        this.amount = amount;
    }

    public static TransferDecoder decode(Transaction trx, String tokenAddress) {
        String input = trx.getInput();
        if (input.substring(0, 10).equals("0xa9059cbb")) {
            if (tokenAddress.equals("0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")) {
                return new TransferDecoder(trx.getTo(), trx.getValue());
            } else {
                return new TransferDecoder(input.substring(10, 74), new BigInteger(input.substring(74, 138), 16));
            }
        }
        return new TransferDecoder("0x0", BigInteger.ZERO);
    }

}
