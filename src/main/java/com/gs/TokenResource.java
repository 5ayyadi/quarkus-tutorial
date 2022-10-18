package com.gs;

import com.core.models.Token;
import com.core.schemas.request.TokenARequest;
import com.core.schemas.request.TokenRequest;

import io.quarkus.logging.Log;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.annotations.Filter;
import com.core.customTypes.Address;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param address
 * @return
 */
@Path("/token")
public class TokenResource {

    TokenRepository tokenRepository;
    WalletRepository walletRepository;

    public TokenResource(TokenRepository tokenRepository, WalletRepository walletRepository) {
        this.tokenRepository = tokenRepository;
        this.walletRepository = walletRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Token> tokensList(
            @QueryParam("token") String tokenSymbol,
            @QueryParam("address") String tokenAddress
    // @QueryParam("tokenId") Long tokenId,
    ) {
        // TODO filters ....
        // return tokenRepository.list("address", null);
        // String q = null;
        // Map<String, Object> qp = new HashMap<String, Object>();

        if (tokenSymbol != null) {
            // q = "symbol";
            // qp.put("symbol", tokenSymbol);
            return tokenRepository.listBySymbol(tokenSymbol);

        }
        if (tokenAddress != null) {
            // if (q != null)
            // q += ",address";
            // else
            // q = "address";
            // qp.put("address", tokenAddress);
            return tokenRepository.listByAddress(tokenAddress);
        }
        return tokenRepository.listAll();
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(TokenRequest request) {
        System.out.println(request.address);
        Token token = tokenRepository.create(request);
        return Response.status(Status.CREATED).entity(token).build();
    }

}