package com.gs;

import java.util.List;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.quarkus.logging.Log;

@Path("/wallet")
public class WalletResource {

    WalletRepository walletRepository;

    public WalletResource(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Wallet> wallets(@QueryParam("address") String address) {
        if (address != null) {
            Log.infof("Searching for %s", address);
            return walletRepository.findByAddress(address);
        }
        return Wallet.listAll();
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Wallet wallet) {
        wallet.id = null;
        wallet.persist();
        return Response.status(Status.CREATED).entity(wallet).build();
    }

}