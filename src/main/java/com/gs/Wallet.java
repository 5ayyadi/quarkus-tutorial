package com.gs;

import java.util.List;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Wallet extends PanacheEntity {

    //TODO: add setter to this class
    public String address;
    public Long balance;

    public String getAddress(){
        return this.address;
    }

    public Long getBalance(){
        return this.balance;
    }

    public static List<Wallet> findByAddress(String address) {
        return find("address", address).list();
    }

}