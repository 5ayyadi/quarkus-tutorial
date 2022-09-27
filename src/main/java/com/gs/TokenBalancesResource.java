package com.gs;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.core.models.Token;
import com.core.models.TokenBalances;
import com.core.models.Wallet;
import io.quarkus.logging.Log;

@Path("/tokenbalances")
public class TokenBalancesResource {

    // find user by address
    // find token by address
    // get balances of the user on the token via blockchain
    // persist on token balances

    final public String rpc = "https://bsc-dataseed1.binance.org/";

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response userBalance(TokenBalances request) {
        Token token = new Token(request.token);
        Wallet wallet = new Wallet(request.wallet);
        TokenBalances tokenBalances(token, wallet, request.balance);

        return Response.status(Status.CREATED).entity(request).build();
    }
}
