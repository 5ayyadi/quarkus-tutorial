package com.core.wallet;

import java.math.BigInteger;

import com.core.customTypes.Address;
import org.web3j.protocol.core.methods.response.Transaction;
import com.core.models.wallet.Wallet;
import com.core.network.Network;
import com.core.schemas.request.WithdrawDepositRequest;
import com.core.schemas.trxDecoder.TransferDecoder;

import io.quarkus.logging.Log;

public class Deposit {

    public static boolean isValid(WithdrawDepositRequest request, Wallet wallet) {
        return Deposit.isValid(request.trxHash, request.tokenAddress, request.amount, request.network, wallet);
    }

    public static boolean isValid(String trxHash, Address tokenAddress, BigInteger amount, Network network,
            Wallet wallet) {
        try {
            // TODO - think on how to save trxReciept ...
            Transaction trx = network.value.w3.ethGetTransactionByHash(trxHash).send().getResult();
            TransferDecoder inputData = TransferDecoder.decode(trx, tokenAddress);
            return (inputData.address.equals(wallet.getAddress())
                    && inputData.amount.equals(amount));
        } catch (Exception e) {
            Log.error(e);
            return false;
        }
    }
}
