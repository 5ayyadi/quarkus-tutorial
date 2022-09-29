package com.core.workers;

import java.util.List;

import com.core.models.TransactionStatus;
import com.core.models.TransactionType;
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
                // get source and destination token balance
                // add balance to destination
                // subtract from source
                break;
                case DEPOSIT:
                // add balance to destination
                break;
                case WITHDRAW:
                // subtract balance from source
            }
            // change status to success
        }

    }
    
}
