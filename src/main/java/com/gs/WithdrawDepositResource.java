package com.gs;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.core.models.TransactionStatus;
import com.core.models.wallet.Wallet;
import com.core.schemas.request.WithdrawDepositRequest;
import com.core.wallet.Deposit;


@Path("/transaction")
public class WithdrawDepositResource {

    public WalletRepository walletRepository;
    public TokenBalanceRepository tokenBalanceRepository;


    public WithdrawDepositResource(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Path("/deposit")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response depositAddress(@QueryParam("user_id") Long userId){
        Wallet resWallet = walletRepository.findByUserId(userId);
        return Response.status(Status.OK).entity(resWallet).build();
    }

    @Path("/deposit")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deposit(WithdrawDepositRequest request){
        request.changeStatus(TransactionStatus.PENDING);
        Wallet resWallet = walletRepository.findByUserId(request.userId);
        if(Deposit.isValid(request, resWallet)){
            request.changeStatus(TransactionStatus.CONFIRMED);
            resWallet.deposit(request, tokenBalanceRepository);
        };
        return Response.status(Status.OK).entity(resWallet).build();
    }
        
    @Path("/withdraw")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response withdraw(WithdrawDepositRequest request) {
        request.changeStatus(TransactionStatus.PENDING);
        Wallet resWallet = walletRepository.findByUserId(request.userId);
        // withdraw in blockchain
        // blockchain.withdraw(request);
        resWallet.withdraw(request, tokenBalanceRepository);
        return Response.status(Status.OK).entity(request).build();
    }
    
}
