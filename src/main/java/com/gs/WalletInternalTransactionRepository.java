package com.gs;

import javax.enterprise.context.ApplicationScoped;

import org.bitcoinj.wallet.WalletTransaction;

import com.core.models.wallet.Wallet;
import com.core.models.wallet.WalletInternalTransactions;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class WalletInternalTransactionRepository implements PanacheRepository<WalletInternalTransactions> {

    public WalletInternalTransactions findByPublicKey(String address) {
        return find("lower(publickey)", address.toLowerCase()).firstResult();
    }

    public WalletInternalTransactions findByUserId(Long userId) {
        return find("userId", userId).firstResult();
    }

}