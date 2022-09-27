package com.core.wallet;


import com.core.math.Decimal;
import com.core.models.Wallet;

public class WalletExternalTransactions extends WalletTransactions{
    public WalletExternalTransactions(Wallet wallet){
            super(wallet);
        }

    @Override
    public boolean transfer(Wallet toWallet, Decimal value) {
        try{
            token.contract.transfer(toWallet.publicKey, value.toBigInteger()).send();
            
        }
        catch(Exception e){
            return false;
        }
        return true;

    }
    }

