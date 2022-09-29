package com.gs;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.core.models.Token;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class TokenRepository implements PanacheRepository<Token> {

    public Token findByAddress(String address) {
        return find("lower(address)", address.toLowerCase()).firstResult();
    }

}
