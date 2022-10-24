package com.core.repositories;

import javax.enterprise.context.ApplicationScoped;

import org.bitcoinj.wallet.WalletTransaction;

import com.core.models.wallet.Wallet;
import com.core.models.wallet.WalletExternalTransactions;
import com.core.models.wallet.WalletInternalTransactions;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class WalletInternalTransactionRepository implements PanacheRepository<WalletInternalTransactions> {

    public WalletInternalTransactions findByUserId(Long userId) {
        return find("userId", userId).firstResult();
    }

    // public WalletInternalTransactions
    // findByExternalInternalTransaction(WalletExternalTransactions
    // externalTransaction) {
    // return find("externalTrx", ).firstResult();
    // }

}