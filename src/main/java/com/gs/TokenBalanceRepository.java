package com.gs;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import com.core.models.TokenBalances;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class TokenBalanceRepository implements PanacheRepository<TokenBalances> {
    public String getTokenBalance(Long wallet_id, Long token_id) {
        // TODO - Makes this dynamic
        String q = String.format(
                "SELECT balance from book" +
                        " JOIN token_balance tb on token.id = tb.token_id " +
                        "join wallet w on w.id = tb.wallet_id" +
                        " where w.id like '%s' and token.id like '%s'",
                wallet_id, token_id);
        Query query = this.getEntityManager().createNativeQuery(q);
        return (String) query.getSingleResult();

    }
}
