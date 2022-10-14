package com.core.models;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@MappedSuperclass
public class PanacheEntityWithTime extends PanacheEntity {

    public PanacheEntityWithTime() {
    }

    public Timestamp created;
    public Timestamp modified;
    public boolean deleted;

    // @Version
    // public int version;

    @PrePersist
    private void prePersist() {
        // created = LocalDateTime.now(Clock.systemUTC());
        created = new Timestamp((new Date()).getTime());
        modified = created;
    }

    @PreUpdate
    private void preUpdate() {
        modified = new Timestamp((new Date()).getTime());
    }

    @PreRemove
    private void preRemove() {
        throw new UnsupportedOperationException("not support remove operation");
    }
}
