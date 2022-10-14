package com.core.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
public class PanacheEntityBaseWithTime extends PanacheEntityBase {

    public PanacheEntityBaseWithTime() {
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
