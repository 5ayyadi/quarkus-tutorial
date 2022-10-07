package com.gs;

import javax.enterprise.context.ApplicationScoped;

import com.core.models.Token;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class TokenRepository implements PanacheRepository<Token> {

    public Token findByAddress(String address) {
        return find("lower(address)", address.toLowerCase()).firstResult();
    }

    public Token findByTokenId(Long token_id) {
        return find("token_id", token_id).firstResult();
    }

}
