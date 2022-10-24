package com.core.repositories;

import javax.enterprise.context.ApplicationScoped;

import com.core.customTypes.Address;
import com.core.models.wallet.MasterWallet;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class MasterWalletRepository implements PanacheRepository<MasterWallet> {

    public MasterWallet findByPublicKey(String pk) {
        return find("lower(publicKey)", pk.toLowerCase()).firstResult();
    }
    
    public MasterWallet findByAddress(String address) {
        return find("address", address).firstResult();
    }

    public MasterWallet findByAddress(Address address) {
        return this.findByAddress(address.toString());
    }


}