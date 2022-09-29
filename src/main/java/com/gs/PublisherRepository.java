package com.gs;

import javax.enterprise.context.ApplicationScoped;

import com.core.models.Publisher;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PublisherRepository implements PanacheRepository<Publisher> {

    public Publisher findByName(String name) {
        return find("lower(name)", name.toLowerCase()).firstResult();
    }

}
