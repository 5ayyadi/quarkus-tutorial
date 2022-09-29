package com.gs;

import com.core.models.Wallet;
import com.core.wallet.WalletInternalTransactions;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;;

@Path("/transfer")
public class TransferResource {

    WalletRepository walletRepository;

    public TransferResource(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(Transfer transfer) {
        // TODO - Add custom responses
        Wallet from = walletRepository.findByAddress(transfer.getFrom());
        // if(from.ValueBalance > transfer.getAmount()){
        // return Response.status(Status.BAD_REQUEST).entity(transfer).build();
        // }
        Wallet to = walletRepository.findByAddress(transfer.getTo());
        System.out.println(to);
        System.out.println(from);
        // WalletInternalTransactions x = new WalletInternalTransactions(from);

        return Response.status(Status.OK).entity(transfer).build();

        // to.balance += transfer.getAmount();
        // from.balance -= transfer.getAmount();
    }

}