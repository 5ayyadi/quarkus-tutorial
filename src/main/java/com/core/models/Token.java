package com.core.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Token extends PanacheEntity {

    public String name;

    public String symbol;

    @Column(updatable = false, unique = true)
    public String address;

    public int decimals;  
    


}
