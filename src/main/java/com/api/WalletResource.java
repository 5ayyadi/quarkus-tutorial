package com.api;

import java.util.ArrayList;
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

import com.core.models.wallet.Wallet;
import com.core.repositories.WalletInternalTransactionRepository;
import com.core.repositories.WalletRepository;
import com.core.schemas.request.WalletCreationRequest;

import io.quarkus.logging.Log;

@Path("/wallet")
public class WalletResource {

    WalletRepository walletRepository;
    WalletInternalTransactionRepository walletInternalTransactionRepository;

    public WalletResource(WalletRepository walletRepository,
            WalletInternalTransactionRepository walletInternalTransactionRepository) {
        this.walletRepository = walletRepository;
        this.walletInternalTransactionRepository = walletInternalTransactionRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Wallet> wallets(@QueryParam("address") String address) {
        if (address != null) {
            Log.infof("Searching for %s", address);
            ArrayList<Wallet> wallets = new ArrayList<>();
            wallets.add(walletRepository.findByAddress(address));
            return wallets;
        }
        return walletRepository.listAll();
    }

    // TODO: Request body should be controlled to have the specified fields.
    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(WalletCreationRequest walletReq) {
        // TODO - Check if userId has wallet
        Wallet resWallet = walletRepository.findByUserId(walletReq.userId);
        if (resWallet == null) {
            resWallet = new Wallet(walletReq.userId);
            resWallet.persist();
            // walletRepository.persist(wallet);
            // resWallet = wallet;
        }
        return Response.status(Status.CREATED).entity(resWallet).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(Wallet wallet) {
        // Wallet entity = walletRepository.findById(wallet.id);
        // entity.address = wallet.getAddress();
        // entity.balance = wallet.getBalance();
        return Response.status(Status.OK).entity("ASD").build();
    }

    // TODO: The request should be as a json, not a number.
    @Transactional
    @DELETE
    public Response delete(Long id) {
        walletRepository.deleteById(id);
        return Response.status(Status.OK).build();
    }

}