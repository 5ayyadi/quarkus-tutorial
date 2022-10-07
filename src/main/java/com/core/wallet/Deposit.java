package com.core.wallet;



import java.math.BigInteger;

import org.web3j.protocol.core.methods.response.Transaction;
import com.core.models.wallet.Wallet;
import com.core.schemas.request.WithdrawDepositRequest;

import io.quarkus.logging.Log;

public class Deposit {

    public static BigInteger getAmount(String input){
        if(input.substring(0,10).equals("0xa9059cbb")){
            return new BigInteger(input.substring(74,138),16);
        }
        return BigInteger.ZERO;
    }

    public static boolean isValid(WithdrawDepositRequest request, Wallet wallet){
        try{
            BigInteger amount = BigInteger.ZERO;
            Transaction trx = request.network.value.w3.ethGetTransactionByHash(request.trxHash).send().getResult();
            if(request.tokenAddress.equals("0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")){
                amount = trx.getValue();
            } else {
                amount = Deposit.getAmount(trx.getInput());
            }
            return (trx.getTo().equals(wallet.address) && request.amount.equals(amount));
        } catch(Exception e){
            Log.error(e);
            return false;
        }
    }
}
