package com.gs;

import com.core.models.TransactionStatus;
import com.core.models.wallet.Wallet;
import com.core.models.wallet.WalletInternalTransactions;
import com.core.schemas.request.TransferRequest;
import com.google.inject.Inject;

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
    TokenBalanceRepository tokenBalanceRepository;
    WalletInternalTransactionRepository wltTrxRepo;
    TokenRepository tokenRepo;

    public TransferResource(WalletRepository walletRepository, TokenBalanceRepository tokenBalanceRepository,
            WalletInternalTransactionRepository wltTrxRepo, TokenRepository tokenRepo) {
        this.walletRepository = walletRepository;
        this.tokenBalanceRepository = tokenBalanceRepository;
        this.wltTrxRepo = wltTrxRepo;
        this.tokenRepo = tokenRepo;
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(TransferRequest request) {
        WalletInternalTransactions trx = Transfer.transfer(request, tokenRepo, walletRepository, wltTrxRepo, tokenBalanceRepository);
        // see if transaction is in the database
        if(trx.equals(null)){
            return Response.status(Status.NOT_ACCEPTABLE).entity(request).build();
        }
        return Response.status(Status.OK).entity(trx).build();

        // request.changeStatus(TransactionStatus.PENDING);
        // Wallet fromWallet = walletRepository.findByUserId(request.fromUID);
        // Wallet toWallet = walletRepository.findByUserId(request.toUID);
        // if (fromWallet.hasBalance(request, tokenBalanceRepository)) {
        // // withdraw in blockchain
        // // blockchain.withdraw(request);
        // fromWallet.withdraw(request, tokenBalanceRepository);
        // toWallet.deposit(request, tokenBalanceRepository);
        // request.changeStatus(TransactionStatus.SUCCESS);
        // return Response.status(Status.OK).entity(request).build();
        // }
        // return Response.status(Status.NOT_ACCEPTABLE).entity(request).build();
    }

}