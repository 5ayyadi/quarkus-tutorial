package com.gs;

import javax.persistence.Column;
import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Transfer extends PanacheEntity {

    @Column()
    public String fromWallet;
    @Column()
    public String toWallet;
    @Column()
    public int amount;

    public void setFrom(String from) {
        this.fromWallet = from;
    }

    public void setTo(String to) {
        this.toWallet = to;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getFrom() {
        return this.fromWallet;
    }

    public String getTo() {
        return this.toWallet;
    }

    public int getAmount() {
        return this.amount;
    }

}