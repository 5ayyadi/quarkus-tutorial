package com.core.wallet;

import java.math.BigInteger;

import org.web3j.protocol.core.methods.response.Transaction;
import com.core.models.wallet.Wallet;
import com.core.schemas.request.WithdrawDepositRequest;
import com.core.schemas.trxDecoder.TransferDecoder;

import io.quarkus.logging.Log;

public class Deposit {

    public static boolean isValid(WithdrawDepositRequest request, Wallet wallet) {
        try {
            // TODO - think on ho to save trxReciept ...
            Transaction trx = request.network.value.w3.ethGetTransactionByHash(request.trxHash).send().getResult();
            TransferDecoder inputData = TransferDecoder.decode(trx, request.tokenAddress);
            return (inputData.address.equals(wallet.address)
                    && inputData.amount.equals(request.amount));
        } catch (Exception e) {
            Log.error(e);
            return false;
        }
    }
}
