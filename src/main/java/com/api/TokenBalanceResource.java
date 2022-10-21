package com.api;

import com.core.models.Token;
import com.core.models.TokenBalances;
import com.core.models.wallet.Wallet;
import com.core.repositories.TokenBalanceRepository;
import com.core.repositories.TokenRepository;
import com.core.repositories.WalletRepository;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/tokenBalance")
public class TokenBalanceResource {

    TokenRepository tokenRepository;
    WalletRepository walletRepository;
    TokenBalanceRepository tokenBalanceRepository;

    public TokenBalanceResource(WalletRepository walletRepository, TokenRepository tokenRepository,
            TokenBalanceRepository tokenBalanceRepository) {
        this.tokenRepository = tokenRepository;
        this.walletRepository = walletRepository;
        this.tokenBalanceRepository = tokenBalanceRepository;
    }

    @GET
    @Path("/{walletId}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<TokenBalances> balanceById(
            @QueryParam("tokenId") Long tokenId,
            @PathParam("walletId") Long walletId

    ) {
        Wallet wallet = walletRepository.findById(walletId);
        if (tokenId != null) {
            Token token = tokenRepository.findById(tokenId);
            Set<TokenBalances> res = new HashSet<TokenBalances>();
            res.add(wallet.getTokenBalances(token));
            return res;
        } else {
            return wallet.getTokenBalances();
        }
    }

    @GET
    @Path("/byAddress/")
    @Produces(MediaType.APPLICATION_JSON)
    public String balanceByAddress(
            @QueryParam("tokenAddress") String tokenAddress,
            @QueryParam("walletAddress") String walletAddress

    ) {
        return tokenBalanceRepository.getTokenBalance(walletAddress, tokenAddress);
    }

    // @Transactional
    // @POST
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // public void create(TokenBalancesRequest request) {
    // TokenBalanceRepository.addTokenBalances(request, tokenRepository,
    // walletRepository);
    // }
}
