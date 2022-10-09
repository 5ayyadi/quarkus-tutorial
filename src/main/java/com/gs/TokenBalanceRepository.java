package com.gs;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import com.core.models.TokenBalances;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;

@ApplicationScoped
public class TokenBalanceRepository implements PanacheRepository<TokenBalances> {
        public String getTokenBalance(Long wallet_id, Long token_id) {
                // TODO - Makes this dynamic
                String q = String.format(
                                "SELECT balance from token" +
                                                " JOIN token_balance tb on token.id = tb.token_id " +
                                                "join wallet w on w.id = tb.wallet_id" +
                                                " where w.id = %s and token.id = %s",
                                wallet_id, token_id);
                Query query = this.getEntityManager().createNativeQuery(q);
                return (String) query.getSingleResult();
        }

        public String getTokenBalance(String wallet_address, String token_address) {
                // TODO - Makes this dynamic
                try{
                        String q = String.format(
                                        "SELECT balance from token" +
                                                        " JOIN token_balance tb on token.id = tb.token_id " +
                                                        "join wallet w on w.id = tb.wallet_id" +
                                                        " where w.address = '%s' and token.address = '%s'",
                                        wallet_address, token_address);
                        Query query = this.getEntityManager().createNativeQuery(q);
                        return (String) query.getSingleResult();
                } catch(Exception e){
                        Log.errorf("Error at token Balance in token repo: %s", e.getMessage());
                        return null;
                }
        }

        public boolean changeTokenBalance(Long wallet_id, Long token_id, String newBalance) {
                String q = String.format(
                                "SELECT balance from token" +
                                                " JOIN token_balance tb on token.id = tb.token_id " +
                                                "join wallet w on w.id = tb.wallet_id" +
                                                " where w.id = %s and token.id = %s",
                                wallet_id, token_id);
                Query query = this.getEntityManager().createNativeQuery(q);
                String r = (String) query.getSingleResult();
                return true;
        }

        public boolean changeTokenBalance(String wallet_address, String token_address, String newBalance) {
                String q = String.format(
                                "SELECT balance from token" +
                                                " JOIN token_balance tb on token.id = tb.token_id " +
                                                "join wallet w on w.id = tb.wallet_id" +
                                                " where w.address = '%s' and token.address = '%s'",
                                wallet_address, token_address);
                Query query = this.getEntityManager().createNativeQuery(q);
                String r = (String) query.getSingleResult();
                return true;

        }
}
