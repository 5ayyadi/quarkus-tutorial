package com.gs;

import javax.enterprise.context.ApplicationScoped;

import com.core.models.wallet.Wallet;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class WalletRepository implements PanacheRepository<Wallet> {

    public Wallet findByAddress(String address) {
        return find("lower(address)", address.toLowerCase()).firstResult();
    }

    public Wallet findByUserId(Long userId) {
        return find("userId", userId).firstResult();
    }

}