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

import com.core.models.Wallet;
import io.quarkus.logging.Log;


@Path("/balanceOf")
public class ContractCallResource{

    final public String privateKey = "22d78e2e73a25be105d1ee5c050070c14bd9819fea5faab76bb26674a7d2a8309a8a7d0ede63c7cf5ec2acc96040ea34e4313e0001ace91295838871ded7995d";
    final public String tokenAddress = "0x04068da6c83afcfa0e13ba15a6696662335d5b75";
    final public String rpc = "https://rpcapi.fantom.network";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public BigInteger balanceOf(@QueryParam("walletAddress") String address) {
        if (address != null) {
            Token token = new Token(tokenAddress,rpc,privateKey);
            try{
                return token.contract.balanceOf(address).send();
            }
            catch(Exception e){
                return BigInteger.ZERO;
            }
        }
        return BigInteger.ZERO;
    }
}
