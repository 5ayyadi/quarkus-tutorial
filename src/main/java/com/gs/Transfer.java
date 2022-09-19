package com.gs;


import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Transfer extends PanacheEntity {

    private String from;
    private String to;
    private Long amount;

    public void setFrom(String from){
        this.from = from;
    }

    public void setTo(String to){
        this.to = to;
    }

    public void setAmount(Long amount){
        this.amount = amount;
    }

    public String getFrom(){
        return this.from;
    }

    public String getTo(){
        return this.to;
    }

    public Long getAmount(){
        return this.amount;
    }
    
}