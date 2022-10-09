package com.gs;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.web3j.abi.datatypes.Address;

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

    public Token tokenFromAddress(Network network, Address address) {
        Token token = getByAddress(address);
        if (token != null) {
            return token;
        }
        try {
            ERC20 erc20TokenContract = ERC20.load(address.toString(), network.value.config.w3);
            token = new Token(
                    erc20TokenContract.name().send(),
                    erc20TokenContract.symbol().send(),
                    address.toString(),
                    erc20TokenContract.decimals().send().intValue(),
                    network);
        } catch (Exception e) {
            e.printStackTrace();
            token = null;
        }
        return token;
    }

    public boolean create(TokenRequest request) {
        try {
            if (getByAddress(request.address) == null) {
                Token token = tokenFromAddress(request.network, request.getAddress());
                token.persist();
            }
            return true;
        } catch (Exception e) {
            Log.errorf("at Token.create request: %s, error: %s", request, e);
            return false;

        }

    }

}
