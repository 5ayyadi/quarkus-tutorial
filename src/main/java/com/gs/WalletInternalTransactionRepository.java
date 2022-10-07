package com.gs;

import javax.enterprise.context.ApplicationScoped;

import org.bitcoinj.wallet.WalletTransaction;

import com.core.models.wallet.Wallet;
import com.core.models.wallet.WalletInternalTransactions;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class WalletInternalTransactionRepository implements PanacheRepository<WalletInternalTransactions> {

    public WalletInternalTransactions findByAddress(String address) {
        return find("lower(address)", address.toLowerCase()).firstResult();
    }

    public WalletInternalTransactions findByUserId(Long userId) {
        return find("userId", userId).firstResult();
    }

}