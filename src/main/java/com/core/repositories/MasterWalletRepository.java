package com.core.repositories;

import javax.enterprise.context.ApplicationScoped;

import com.core.models.wallet.MasterWallet;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class MasterWalletRepository implements PanacheRepository<MasterWallet> {

    public MasterWallet findByPublicKey(String pk) {
        return find("lower(publicKey)", pk.toLowerCase()).firstResult();
    }
    

}