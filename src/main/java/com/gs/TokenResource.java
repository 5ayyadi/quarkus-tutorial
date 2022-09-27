package com.gs;

import com.core.models.Token;
import com.core.models.Wallet;
import com.core.wallet.WalletInternalTransactions;
import io.quarkus.logging.Log;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Path("/token")
public class TokenResource {

    TokenRepository tokenRepository;

    public TokenResource(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Token> wallets(@QueryParam("address") String address) {
        if (address != null) {
            Log.infof("Searching for %s", address);
            return tokenRepository.findByAddress(address);
        }
        return Token.listAll();
    }


    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(Token token) {
//        Token _token = tokenRepository.findByAddress(token.address);
//        if (_token == null) {
//        }
            token.persist();

        return Response.status(Status.CREATED).entity(token).build();

    }

}