package com.gs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Parameter;
import javax.persistence.Query;

import com.core.customTypes.Address;

import com.core.models.wallet.Wallet;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

@ApplicationScoped
public class WalletRepository implements PanacheRepository<Wallet> {

    public Wallet findByPublicKey(String publicKey) {
        return find("lower(publickey)", publicKey.toLowerCase()).firstResult();
    }

    public Wallet findByAddress(String address) {
        return find("address", address).firstResult();
    }

    public Wallet findByAddress(Address address) {
        return find("address", address).firstResult();
    }

    public Wallet findByUserId(Long userId) {
        return find("userId", userId).firstResult();
    }

    public List<Address> allWalletAddress() {
        String q = "SELECT address from wallet";
        Query queryObj = this.getEntityManager().createNativeQuery(q);
        return (List<Address>) queryObj.getResultList();
    }

    public Map<Address, Long> allWalletAddressMapping() {
        // String q = "SELECT address,id from wallet";
        // Query queryObj = this.getEntityManager().createNativeQuery(q);
        // List<Parameters> r = queryObj.getResultList();
        // // List<STH> r = (List<STH>) queryObj.getResultList();
        // queryObj.getParameters();
        // return (Map<String, Long>) queryObj.getResultList();
        HashMap<Address, Long> res = new HashMap<>();
        for (Wallet wallet : listAll()) {
            res.put(wallet.getAddress(), wallet.id);
        }
        return res;
    }

}