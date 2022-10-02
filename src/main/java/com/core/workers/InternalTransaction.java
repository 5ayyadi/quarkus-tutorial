package com.core.workers;

import java.math.BigInteger;
import java.util.List;

import com.core.models.TokenBalances;
import com.core.models.TransactionStatus;
import com.core.models.Wallet;
import com.core.wallet.WalletInternalTransactions;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

public class InternalTransaction extends PanacheEntity {

    public void confirmedTransactions(){
        List<WalletInternalTransactions> confirmedTransactions = 
        WalletInternalTransactions.findByStatus(TransactionStatus.CONFIRMED);
        for(WalletInternalTransactions trx: confirmedTransactions){
            switch(trx.trxType){
                case TRANSFER:
                Wallet source = trx.fromWallet;
                Wallet destination = trx.toWallet;
                TokenBalances sourceBalance = source.getTokenBalances(trx.token);
                TokenBalances destinationBalance = destination.getTokenBalances(trx.token);
                BigInteger sb = new BigInteger(sourceBalance.getBalance());
                BigInteger db = new BigInteger(destinationBalance.getBalance());           
                db.add(trx.amount);
                sb.subtract(trx.amount);
                break;
                case DEPOSIT:
                destination = trx.toWallet;
                destinationBalance = destination.getTokenBalances(trx.token);
                db = new BigInteger(destinationBalance.getBalance());
                db.add(trx.amount);
                break;
                case WITHDRAW:
                source = trx.fromWallet;
                sourceBalance = source.getTokenBalances(trx.token);
                sb = new BigInteger(sourceBalance.getBalance());
                sb.subtract(trx.amount);
            }
            trx.trxStatus = TransactionStatus.SUCCESS;
            trx.persist();
        }

    }
    
}
