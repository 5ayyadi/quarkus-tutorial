package com.gs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import com.core.customTypes.Address;

import com.core.models.Token;
import com.core.network.ERC20;
import com.core.network.Network;
import com.core.schemas.request.TokenRequest;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;

@ApplicationScoped
public class TokenRepository implements PanacheRepository<Token> {

    public Token getByAddress(String address) {
        return find("lower(address)", address.toLowerCase()).firstResult();
    }

    public Token getByAddress(Address address) {
        return this.getByAddress(address.toString());
    }

    public List<Token> listByAddress(String address) {
        return list("lower(address)", address.toLowerCase());
    }

    public Token getBySymbol(String symbol) {
        return find("symbol", symbol).firstResult();
    }

    public List<Token> listBySymbol(String symbol) {
        return list("symbol", symbol);
    }

    public Token findByTokenId(Long token_id) {
        return find("token_id", token_id).firstResult();

    }

    public List<String> allTokenAddress() {
        String q = "SELECT address from wallet";
        Query queryObj = this.getEntityManager().createNativeQuery(q);
        return (List<String>) queryObj.getResultList();
    }

    public Map<Address, Long> allTokenAddressMapping() {
        HashMap<Address, Long> res = new HashMap<>();
        for (Token token : listAll()) {
            res.put(token.getAddress(), token.id);
        }
        return res;
    }

    public Token tokenFromAddress(Network network, Address address, boolean shouldPersistIfExists) {
        Token token = getByAddress(address);
        if (token != null) {
            return token;
        }
        try {
            ERC20 erc20TokenContract = ERC20.load(address, network.value.config.w3);
            token = new Token(
                    erc20TokenContract.name().send(),
                    erc20TokenContract.symbol().send(),
                    address,
                    erc20TokenContract.decimals().send().intValue(),
                    network);

            try {

                if (shouldPersistIfExists)
                    token.persist();
            } catch (Exception e) {
                // TODO: handle exception
                Log.error(e);
            }
        } catch (Exception e) {
            // e.printStackTrace();
            Log.error(e);
            token = null;
        }
        return token;
    }

    public Token create(TokenRequest request) {

        return tokenFromAddress(
                request.network,
                request.address,
                true);

    }

}
