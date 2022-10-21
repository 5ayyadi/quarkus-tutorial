package com.api;

import java.math.BigInteger;

import com.core.models.Token;
import com.core.models.TransactionStatus;
import com.core.models.TransactionType;
import com.core.models.wallet.Wallet;
import com.core.models.wallet.WalletInternalTransactions;
import com.core.repositories.TokenBalanceRepository;
import com.core.repositories.TokenRepository;
import com.core.repositories.WalletInternalTransactionRepository;
import com.core.repositories.WalletRepository;
import com.core.schemas.request.TransferRequest;
import com.core.schemas.request.WithdrawDepositRequest;

import io.quarkus.logging.Log;

public class Withdraw {

    public static WalletInternalTransactions internalTransfer(WithdrawDepositRequest request,
            TokenRepository tknRepo,
            TokenBalanceRepository tbRepo,
            WalletInternalTransactionRepository internalRepo,
            WalletRepository wltRepo) {
        // creating transfer request
        TransferRequest transferRequest = new TransferRequest(
                request.userId,
                wltRepo.findByAddress(request.serverWallet.toString()).userId,
                request.tokenId,
                request.amount,
                TransactionType.TRANSFER,
                TransactionStatus.RECEIVED);
        // calling transfer on this request
        WalletInternalTransactions trx = Transfer.transfer(transferRequest, tknRepo, wltRepo, internalRepo, tbRepo);
        return trx;

    }

    public static Boolean externalTransfer(WithdrawDepositRequest request,
            TokenRepository tknRepo,
            TokenBalanceRepository tbRepo,
            WalletInternalTransactionRepository internalRepo,
            WalletRepository wltRepo) {

        try{
            
            Wallet masterWallet = wltRepo.findByAddress(request.serverWallet.toString());
            Token token = tknRepo.getByAddress(request.tokenAddress.toString());
            Boolean res = masterWallet.externalTransfer(request.userWallet, token, request.amount, request.network);
            
            // changing master wallet amount
            BigInteger balance = new BigInteger(tbRepo.getTokenBalance(masterWallet.id, token.id));
            BigInteger newBalance = balance.subtract(request.amount);
            tbRepo.changeTokenBalance(masterWallet.id, token.id, newBalance.toString());    
            return true;
        } catch(Exception e){
            Log.error(e);
            return false;
        }

    }

}
