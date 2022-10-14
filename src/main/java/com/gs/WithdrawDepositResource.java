package com.gs;

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

import com.core.models.TransactionStatus;
import com.core.models.wallet.Wallet;
import com.core.schemas.request.WithdrawDepositRequest;
import com.core.wallet.Deposit;

@Path("/transaction")
public class WithdrawDepositResource {

    public TokenBalanceRepository tokenBalanceRepository;
    public WalletRepository walletRepository;
    public TokenRepository tokenRepository;

    public WithdrawDepositResource(
            WalletRepository walletRepository,
            TokenBalanceRepository tokenBalanceRepository,
            TokenRepository tokenRepository) {

        this.walletRepository = walletRepository;
        this.tokenBalanceRepository = tokenBalanceRepository;
        this.tokenRepository = tokenRepository;
    }

    @Path("/deposit")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response depositAddress(@QueryParam("user_id") Long userId) {
        Wallet resWallet = walletRepository.findByUserId(userId);
        return Response.status(Status.OK).entity(resWallet).build();
    }

    @Transactional
    @Path("/deposit")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deposit(WithdrawDepositRequest request) {
        request.changeStatus(TransactionStatus.PENDING);
        // TODO Why request has wallet address and why you got the user for it
        Wallet resWallet = walletRepository.findByUserId(request.userId);
        if (Deposit.isValid(request, resWallet)) {
            request.changeStatus(TransactionStatus.CONFIRMED);
            resWallet.deposit(
                    request, tokenBalanceRepository, tokenRepository, walletRepository);
            return Response.status(Status.OK).entity(resWallet).build();
        }
        return Response.status(Status.BAD_REQUEST).entity(resWallet).build();
    }

    @Path("/withdraw")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response withdraw(WithdrawDepositRequest request) {
        request.changeStatus(TransactionStatus.PENDING);
        Wallet resWallet = walletRepository.findByUserId(request.userId);
        if (resWallet.hasBalance(request, tokenBalanceRepository)) {
            // withdraw in blockchain
            // blockchain.withdraw(request);
            resWallet.withdraw(request, tokenBalanceRepository);
            return Response.status(Status.OK).entity(request).build();

        }
        return Response.status(Status.NOT_ACCEPTABLE).entity(request).build();
    }

}
