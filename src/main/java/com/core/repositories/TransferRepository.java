package com.core.repositories;

import java.math.BigInteger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.core.models.Token;
import com.core.models.TokenBalances;
import com.core.models.TransactionStatus;
import com.core.models.wallet.Wallet;
import com.core.models.wallet.WalletInternalTransactions;
import com.core.schemas.request.TransferRequest;
import com.core.schemas.request.WithdrawDepositRequest;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;

@ApplicationScoped
public class TransferRepository implements PanacheRepository<TokenBalances> {
    @Inject
    WalletRepository walletRepository;
    @Inject
    TokenRepository tokenRepository;
    @Inject
    TokenBalanceRepository tokenBalanceRepository;
    @Inject
    WalletInternalTransactionRepository internalTransactionRepository;

    public WalletInternalTransactions transfer(TransferRequest request) {
        Wallet fromWallet = walletRepository.findByUserId(request.fromUID);
        Wallet toWallet = walletRepository.findByUserId(request.toUID);
        Token token = tokenRepository.findById(request.tokenId);

        TokenBalances fromWalletTB = tokenBalanceRepository.getTokenBalances(fromWallet, token);
        TokenBalances toWalletTB = toWallet.getTokenBalances(token);
        // TODO throw error here ...
        if (fromWalletTB.getBalance().compareTo(request.amount) > 0) {
            WalletInternalTransactions wit = new WalletInternalTransactions(
                    token, fromWallet, toWallet, request.amount);
            wit.persist();
            toWalletTB.setBalance((toWalletTB.getBalance().add(request.amount)));
            fromWalletTB.setBalance((fromWalletTB.getBalance().subtract(request.amount)));
            wit.status = TransactionStatus.SUCCESS;
            return wit;
        }
        return null;
    }
    // public bool
}