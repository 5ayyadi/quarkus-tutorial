package com.core.repositories;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.core.models.Token;
import com.core.models.TokenBalances;
import com.core.models.wallet.Wallet;
import com.core.schemas.request.WithdrawDepositRequest;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;

@ApplicationScoped
public class TokenBalanceRepository implements PanacheRepository<TokenBalances> {

    // TODO
    // public TokenBalances getTBbyId(Long wallet_id, Long token_id) {
    // return findById(
    // _executeQuery(
    // wallet_id, token_id,
    // String.format(
    // "SELECT id from token" +
    // " JOIN token_balance tb on token.id = tb.token_id " +
    // "join wallet w on w.id = tb.wallet_id" +
    // " where w.id = %s and token.id = %s",
    // wallet_id, token_id)));
    // }

    public static void addTokenBalances(
            WithdrawDepositRequest request,
            TokenRepository tokenRepository,
            WalletRepository walletRepository) {
        Wallet wallet = walletRepository.findByUserId(request.userId);
        if (wallet != null) {
            TokenBalances tb = new TokenBalances();
            tb.setBalance(request.amount.toString());
            Token token = tokenRepository.getByAddress("0x" + request.tokenAddress);
            if (token == null) {
                token = tokenRepository.tokenFromAddress(request.network, request.tokenAddress, false);
            }
            token.addTokenBalances(tb);
            wallet.getTokenBalances().add(tb);
            token.persist();
            tb.setToken(token);
            tb.setWallet(wallet);
            tb.persist();

        }
    }

    private String _executeQuery(Long wallet_id, Long token_id, String query) {
        try {
            Query queryObj = this.getEntityManager().createNativeQuery(query);
            return (String) queryObj.getSingleResult();
        } catch (NoResultException e) {
            // TODO - Why was this line commented ?
            updateTokenBalanceRelation(wallet_id, token_id);
            return "0";
        } catch (Exception e) {
            Log.errorf("Error at token Balance in token repo: %s", e.getMessage());
            return null;
        }
    }

    private String _executeQuery(String wallet_address, String tokenAddress, String query) {
        try {
            Query queryObj = this.getEntityManager().createNativeQuery(query);
            return (String) queryObj.getSingleResult();
        } catch (NoResultException e) {
            updateTokenBalanceRelation(wallet_address, tokenAddress);
            return "0";
        } catch (Exception e) {
            Log.errorf("Error at token Balance in token repo: %s", e.getMessage());
            throw e;
        }
    }

    private void _executeUpdateQuery(String wallet_address, String tokenAddress, String query) {
        Query queryObj = this.getEntityManager().createNativeQuery(query);
        try {
            queryObj.executeUpdate();
        } catch (NoResultException e) {
            System.out.printf("%s, on query : %s%n", e, query);
            updateTokenBalanceRelation(wallet_address, tokenAddress);
        } catch (Exception e) {
            Log.errorf("Error at token Balance in token repo: %s", e.getMessage());
            throw e;
        }
    }

    public void _executeUpdateQuery(Long wallet_address, Long tokenAddress, String query) {
        Query queryObj = this.getEntityManager().createNativeQuery(query);
        try {
            queryObj.executeUpdate();
        } catch (NoResultException e) {
            System.out.printf("%s, on query : %s%n", e, query);
            updateTokenBalanceRelation(wallet_address, tokenAddress);
        } catch (Exception e) {
            Log.errorf("Error at token Balance in token repo: %s", e.getMessage());
            throw e;
        }
    }

    public String getTokenBalance(Long wallet_id, Long token_id) {
        // TODO - Makes this dynamic

        return _executeQuery(
                wallet_id, token_id,
                String.format(
                        "SELECT balance from token" +
                                " JOIN token_balance tb on token.id = tb.token_id " +
                                "join wallet w on w.id = tb.wallet_id" +
                                " where w.id = %s and token.id = %s",
                        wallet_id, token_id));

    }

    public String getTokenBalance(String wallet_address, String tokenAddress) {
        // TODO - Makes this dynamic
        return _executeQuery(wallet_address, tokenAddress, String.format(
                "SELECT balance from token" +
                        " JOIN token_balance tb on token.id = tb.token_id " +
                        "join wallet w on w.id = tb.wallet_id" +
                        " where w.address = '%s' and token.address = '%s'",
                wallet_address, tokenAddress));
    }

    public void changeTokenBalance(Long wallet_id, Long token_id, String newBalance) {
        _executeUpdateQuery(wallet_id, token_id, String.format(
                "UPDATE token_balance set balance ='%s' where id in " +
                        "(SELECT tb.id from token" +
                        " JOIN token_balance tb on token.id = tb.token_id " +
                        "join wallet w on w.id = tb.wallet_id" +
                        " where w.id = '%s' and token.id = '%s')",
                newBalance, wallet_id, token_id));
    }

    public void changeTokenBalance(String wallet_address, String tokenAddress, String newBalance) {
        _executeUpdateQuery(
                wallet_address, tokenAddress,
                String.format(
                        "UPDATE token_balance set balance ='%s' where id in " +
                                "(SELECT tb.id from token" +
                                " JOIN token_balance tb on token.id = tb.token_id " +
                                "join wallet w on w.id = tb.wallet_id" +
                                " where w.address = '%s' and token.address = '%s')",
                        newBalance, wallet_address, tokenAddress));

    }

    public TokenBalances updateTokenBalanceRelation(String wallet_address, String tokenAddress) {
        return updateTokenBalanceRelation(
                Wallet.find("address", wallet_address).firstResult(),
                Token.find("address", tokenAddress).firstResult());

    }

    public TokenBalances updateTokenBalanceRelation(Long wallet_id, Long token_id) {
        Wallet wallet = Wallet.findById(wallet_id);
        Token token = Token.findById(token_id);
        return updateTokenBalanceRelation(
                wallet,
                token);
    }

    private TokenBalances updateTokenBalanceRelation(Wallet wallet, Token token) {
        TokenBalances tb = find(String.format("wallet_id:=wallet and token")).firstResult();
        // TODO - Check if token balance record exist then create it
        if (tb == null) {

            tb = new TokenBalances();
            tb.setBalance("0");
            token.addTokenBalances(tb);
            wallet.getTokenBalances().add(tb);
            token.persist();
            wallet.persist();
            tb.setToken(token);
            tb.setWallet(wallet);
            tb.persist();
        }

        return tb;

    }

    public TokenBalances getTokenBalances(Wallet wallet, Token token) {
        return updateTokenBalanceRelation(wallet, token);
    }

}
