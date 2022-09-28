package com.gs;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.core.models.Token;
import com.core.models.TokenBalances;
import com.core.models.Wallet;
import com.core.network.NetworkConfig;
import com.core.schemas.request.TokenBalancesRequest;

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
    public Response userBalance(TokenBalancesRequest request) {
        Token token = Token.tokenFromAddress(new NetworkConfig(), request.tokenAddress);
        Wallet wallet = Wallet.findByAddress(request.walletAddress);
        TokenBalances tb = new TokenBalances();
        tb.setWallet(wallet);
        tb.setToken(token);
        token.getTokenBalances().add(tb);
        wallet.getTokenBalances().add(tb);
        tb.persist();
        return Response.status(Status.CREATED).entity(request).build();
    }
}
