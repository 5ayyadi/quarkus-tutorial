package com.api;

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
import com.core.repositories.MasterWalletRepository;
import com.core.repositories.TokenBalanceRepository;
import com.core.repositories.TokenRepository;
import com.core.repositories.WalletInternalTransactionRepository;
import com.core.repositories.WalletRepository;
import com.core.schemas.request.WithdrawDepositRequest;
import com.core.wallet.Deposit;

@Path("/transaction")
public class TransactionResource {

    public TokenBalanceRepository tokenBalanceRepository;
    public WalletRepository walletRepository;
    public MasterWalletRepository masterWalletRepository;
    public TokenRepository tokenRepository;
    public WalletInternalTransactionRepository internalRepo;

    public TransactionResource(
            WalletRepository walletRepository,
            TokenBalanceRepository tokenBalanceRepository,
            TokenRepository tokenRepository,
            WalletInternalTransactionRepository internalRepo,
            MasterWalletRepository masterWalletRepository) {

        this.walletRepository = walletRepository;
        this.masterWalletRepository = masterWalletRepository;
        this.tokenBalanceRepository = tokenBalanceRepository;
        this.tokenRepository = tokenRepository;
        this.internalRepo = internalRepo;
    }

    @Path("/deposit")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response depositAddress(@QueryParam("user_id") Long userId) {
        Wallet resWallet = walletRepository.findByUserId(userId);
        if (resWallet == null) {
            return Response.status(Status.NOT_FOUND).entity(null).build();
        }
        return Response.status(Status.OK).entity(resWallet).build();
    }

    @Transactional
    @Path("/deposit")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deposit(WithdrawDepositRequest request) {
        request.tokenId = tokenRepository.getByAddress(request.tokenAddress).id;
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
        request.tokenId = tokenRepository.getByAddress(request.tokenAddress).id;

        Boolean externalTrx = Withdraw.externalTransfer(
                request,
                tokenRepository,
                tokenBalanceRepository,
                internalRepo,
                masterWalletRepository,
                walletRepository);

        WalletInternalTransactions internalTrx = Withdraw.internalTransfer(
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
