package com.gs;

import com.core.models.Token;
import com.core.models.TokenBalances;
import com.core.models.wallet.Wallet;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void transfer(List<String> sAddress, @QueryParam("wallet_address") String walletAddress) {
        Wallet wallet = walletRepository.findByAddress(walletAddress);
        if (wallet != null) {
            for (String address : sAddress) {
                // TODO - Check if token balance record exist then create it
                TokenBalances tb = new TokenBalances();
                tb.setBalance("100500");
                Token token;
                token = tokenRepository.getByAddress(address);
                if (token == null) {
                    token = new Token();
                    token.address = address;
                    token.decimals = 18;
                    token.symbol = address;
                    token.name = address;
                }
                token.addTokenBalances(tb);
                wallet.getTokenBalances().add(tb);
                token.persist();
                tb.setToken(token);
                tb.setWallet(wallet);
                tb.persist();

            }
        }
    }
}
