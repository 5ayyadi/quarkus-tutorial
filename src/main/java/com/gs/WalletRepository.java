package com.gs;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.core.models.Wallet;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class WalletRepository implements PanacheRepository<Wallet> {

    public List<Wallet> findByAddress(String address) {
        return find("lower(address)", address.toLowerCase()).list();
    }

}