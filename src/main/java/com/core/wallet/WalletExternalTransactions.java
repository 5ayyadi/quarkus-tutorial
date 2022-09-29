package com.core.wallet;

import javax.persistence.Entity;

import com.core.math.Decimal;
import com.core.models.Token;
import com.core.models.Wallet;

@Entity
public class WalletExternalTransactions extends WalletTransactions {

    public WalletExternalTransactions() {
    }
    // public WalletExternalTransactions(Wallet wallet) {
    // super(wallet);
    // }

    @Override
    public boolean transfer(Token token, Wallet toWallet, Decimal value) {
        try {
            // token.contract.transfer(toWallet.publicKey, value.toBigInteger()).send();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }
}
