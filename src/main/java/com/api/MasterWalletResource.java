package com.api;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.core.repositories.MasterWalletRepository;
import com.core.schemas.request.MasterWalletRequest;
import com.core.schemas.request.PcmTransferRequest;

@Path("/MasterWallet")
public class MasterWalletResource {

    MasterWalletRepository masterWalletRepository;

    public MasterWalletResource(MasterWalletRepository masterWalletRepository) {
        this.masterWalletRepository = masterWalletRepository;
    }

    @Path("/create")
    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(MasterWalletRequest request) {
        // TODO - Check if userId has wallet
        // String publicKey = MasterWallet.getPublicKeyInHex(request.privateKey);

        // MasterWallet resWallet = masterWalletRepository.findByPublicKey(publicKey);
        // if (resWallet == null) {
        // MasterWallet wallet = new MasterWallet(request.privateKey, publicKey,
        // request.network);
        // masterWalletRepository.persist(wallet);
        // resWallet = wallet;
        // }
        return Response.status(Status.CREATED).entity(null).build();
    }

    @Path("/transfer")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(PcmTransferRequest request) {
        // TODO - Check if userId has wallet
        // String publicKey = MasterWallet.getPublicKeyInHex(request.privateKey);
        // MasterWallet resWallet = masterWalletRepository.findByPublicKey(publicKey);
        // if (resWallet == null) {
        // this.create(request);
        // resWallet = masterWalletRepository.findByPublicKey(publicKey);
        // }
        // Boolean result = resWallet.transferTo(request.tokenAddress, request.toWallet,
        // request.amount);

        return Response.status(Status.CREATED).entity(null).build();
    }
}
