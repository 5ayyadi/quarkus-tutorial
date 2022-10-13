package com.gs;

import com.core.models.TransactionStatus;
import com.core.models.wallet.Wallet;
import com.core.models.wallet.WalletInternalTransactions;
import com.core.models.wallet.WalletTransactionStatus;
import com.core.schemas.request.TransferRequest;

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

    public WalletRepository walletRepository;
    public TokenBalanceRepository tokenBalanceRepository;

    public TransferResource(WalletRepository walletRepository, TokenBalanceRepository tokenBalanceRepository) {
        this.walletRepository = walletRepository;
        this.tokenBalanceRepository = tokenBalanceRepository;
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(TransferRequest request) {
        WalletInternalTransactions trx = new WalletInternalTransactions(request);
        trx.changeStatus(WalletTransactionStatus.PENDING);
        if(trx.isTransferValid()){
            //some code
            trx.wallet.withdraw(request, tokenBalanceRepository);
            trx.toWallet.deposit(request, tokenBalanceRepository);
            trx.changeStatus(WalletTransactionStatus.SUCCESS);
            return Response.status(Status.OK).entity(request).build();
        }
        trx.changeStatus(WalletTransactionStatus.FAILED, "Insufficient Balance");
        return Response.status(Status.NOT_ACCEPTABLE).entity(request).build();


        // request.changeStatus(TransactionStatus.PENDING);
        // Wallet fromWallet = walletRepository.findByUserId(request.fromUID);
        // Wallet toWallet = walletRepository.findByUserId(request.toUID);
        // if (fromWallet.hasBalance(request, tokenBalanceRepository)) {
        //     // withdraw in blockchain
        //     // blockchain.withdraw(request);
        //     fromWallet.withdraw(request, tokenBalanceRepository);
        //     toWallet.deposit(request, tokenBalanceRepository);
        //     request.changeStatus(TransactionStatus.SUCCESS);
        //     return Response.status(Status.OK).entity(request).build();
        // }
        // return Response.status(Status.NOT_ACCEPTABLE).entity(request).build();
    }

}