package com.gs;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.core.models.wallet.PCM_Wallet;
import com.core.schemas.request.PcmRequest;
import com.core.schemas.request.PcmTransferRequest;

@Path("/pcm_wallet")
public class PCM_WalletResource {
    
    PcmRepository pcmRepository;

    public PCM_WalletResource(PcmRepository pcmRepository) {
        this.pcmRepository = pcmRepository;
    }

    

    @Path("/create")
    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(PcmRequest request) {
        // TODO - Check if userId has wallet
        String publicKey = PCM_Wallet.getPublicKeyInHex(request.privateKey);
        PCM_Wallet resWallet = pcmRepository.findByPublicKey(publicKey);
        if (resWallet == null) {
            PCM_Wallet wallet = new PCM_Wallet(request.privateKey, publicKey, request.network);
            pcmRepository.persist(wallet);
            resWallet = wallet;
        }
        return Response.status(Status.CREATED).entity(resWallet).build();
    }

    @Path("/transfer")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(PcmTransferRequest request) {
        // TODO - Check if userId has wallet
        String publicKey = PCM_Wallet.getPublicKeyInHex(request.privateKey);
        PCM_Wallet resWallet = pcmRepository.findByPublicKey(publicKey);
        if (resWallet == null) {
            this.create(request);
            resWallet = pcmRepository.findByPublicKey(publicKey);
        }
        Boolean result = resWallet.transferTo(request.tokenAddress, request.toWallet, request.amount);

        return Response.status(Status.CREATED).entity(result).build();
    }
}
