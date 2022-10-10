package com.gs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Parameter;
import javax.persistence.Query;

import com.core.models.wallet.Wallet;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

class STH extends PanacheEntity {
    @Override
    public String toString() {
        return "STH [address=" + address + ", id=" + id + "]";
    }

    String address;
    Long id;

    public STH() {
        super();
    }

    public STH(String address, Long id) {
        this.address = address;
        this.id = id;
    }

}

@ApplicationScoped
public class WalletRepository implements PanacheRepository<Wallet> {

    public Wallet findByAddress(String address) {
        return find("lower(address)", address.toLowerCase()).firstResult();
    }

    public Wallet findByUserId(Long userId) {
        return find("userId", userId).firstResult();
    }

    public List<String> allWalletAddress() {
        String q = "SELECT address from wallet";
        Query queryObj = this.getEntityManager().createNativeQuery(q);
        return (List<String>) queryObj.getResultList();
    }

    public Map<String, Long> allWalletAddressMapping() {
        // String q = "SELECT address,id from wallet";
        // Query queryObj = this.getEntityManager().createNativeQuery(q);
        // List<Parameters> r = queryObj.getResultList();
        // // List<STH> r = (List<STH>) queryObj.getResultList();
        // queryObj.getParameters();
        // return (Map<String, Long>) queryObj.getResultList();
        HashMap<String, Long> res = new HashMap<>();
        for (Wallet wallet : listAll()) {
            res.put(wallet.address, wallet.id);
        }
        return res;
    }

}