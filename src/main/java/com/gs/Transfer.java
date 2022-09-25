package com.gs;

import javax.persistence.Column;
import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Transfer extends PanacheEntity {

    @Column()
    public String from;
    @Column()
    public String to;
    @Column()
    public int amount;

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }

    public int getAmount() {
        return this.amount;
    }

}