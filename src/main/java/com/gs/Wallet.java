package com.gs;

import java.util.List;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Wallet extends PanacheEntity {

    public String address;

    public Long balance;

    public static List<Wallet> findByAddress(String address) {
        return find("address", address).list();
    }

}