package com.gs;


import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
    public void transfer(Transfer transfer) {
//        TODO - Check balance before hand ...
        Wallet from = walletRepository.findByAddress(transfer.getFrom()).get(0);
        Wallet to = walletRepository.findByAddress(transfer.getTo()).get(0);
        System.out.println(to);
        System.out.println(from);
        to.balance += transfer.getAmount();
        from.balance -= transfer.getAmount(); 
    }

}