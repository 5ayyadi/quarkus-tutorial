package com.gs;

import com.core.models.Token;
import com.core.models.TokenBalances;
import com.core.models.wallet.Wallet;
import com.core.schemas.request.TokenBalancesRequest;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.core.customTypes.Address;

import java.util.List;

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
    @Produces(MediaType.APPLICATION_JSON)
    public String balance(
            @QueryParam("token_address") String token_address,
            @QueryParam("wallet_address") String wallet_address) {
        return tokenBalanceRepository.getTokenBalance(wallet_address, token_address);
    }

    // @Transactional
    // @POST
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // public void create(TokenBalancesRequest request) {
    //     TokenBalanceRepository.addTokenBalances(request, tokenRepository, walletRepository);
    // }
}
