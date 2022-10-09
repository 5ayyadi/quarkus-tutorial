package com.gs;

import javax.enterprise.context.ApplicationScoped;

import com.core.models.wallet.PCM_Wallet;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PcmRepository implements PanacheRepository<PCM_Wallet> {

    public PCM_Wallet findByPublicKey(String pk) {
        return find("lower(publicKey)", pk.toLowerCase()).firstResult();
    }
    

}