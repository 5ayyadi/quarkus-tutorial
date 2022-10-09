package com.gs;

import com.core.models.Token;
import com.core.schemas.request.TokenRequest;

import io.quarkus.logging.Log;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.web3j.abi.datatypes.Address;
import java.util.ArrayList;
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
    public List<Token> wallets(@QueryParam("address") String address) {
        if (address != null) {
            Log.infof("Searching for %s", address);
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(tokenRepository.findByAddress(address));
            return tokens;
        }
        return Token.listAll();
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(TokenRequest request) {
        boolean result = Token.create(request);
        return Response.status(Status.CREATED).entity(result).build();
    }

}