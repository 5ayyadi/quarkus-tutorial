package com.api;

import com.core.models.Token;
import com.core.repositories.TokenRepository;
import com.core.repositories.WalletRepository;
import com.core.schemas.request.TokenRequest;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.List;

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
        Token token = tokenRepository.create(request);
        return Response.status(Status.CREATED).entity(token).build();
    }

}