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
import com.core.models.wallet.WalletInternalTransactions;
import com.core.schemas.request.TransferRequest;
import com.core.schemas.request.WithdrawDepositRequest;
import com.core.wallet.Deposit;

@Path("/transaction")
public class WithdrawDepositResource {

    public TokenBalanceRepository tokenBalanceRepository;
    public WalletRepository walletRepository;
    public TokenRepository tokenRepository;
    public WalletInternalTransactionRepository internalRepo;

    public WithdrawDepositResource(
            WalletRepository walletRepository,
            TokenBalanceRepository tokenBalanceRepository,
            TokenRepository tokenRepository,
            WalletInternalTransactionRepository internalRepo) {

        this.walletRepository = walletRepository;
        this.tokenBalanceRepository = tokenBalanceRepository;
        this.tokenRepository = tokenRepository;
        this.internalRepo = internalRepo;
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
        // TODO: check if the transaction is already in the database
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

    @Transactional
    @Path("/withdraw")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response withdraw(WithdrawDepositRequest request) {
        // internal transfer from user id to server wallet
        WalletInternalTransactions internalTrx = Withdraw.internalTransfer(
                request,
                tokenRepository,
                tokenBalanceRepository,
                internalRepo,
                walletRepository);

        // external transfer from server wallet to user wallet
        Boolean externalTrx = Withdraw.externalTransfer(
                request,
                tokenRepository,
                tokenBalanceRepository,
                internalRepo,
                walletRepository);

        if (internalTrx.status.equals(TransactionStatus.SUCCESS) && externalTrx) {
            return Response.status(Status.OK).entity(request).build();
        }
        return Response.status(Status.BAD_REQUEST).entity(request).build();
    }

}
