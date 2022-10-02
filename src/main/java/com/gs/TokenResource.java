package com.gs;

import com.core.models.Token;
import com.core.models.wallet.Wallet;
import com.core.network.Network;
import com.core.network.TransactionGeneration;
import com.core.network.TransactionGeneration.RawTransactionAndExtraInfo;
import io.quarkus.logging.Log;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @param address
 * @return
 */
@Path("/token")
public class TokenResource {

    TokenRepository tokenRepository;
    WalletRepository walletRepository;

    public TokenResource(TokenRepository tokenRepository, WalletRepository walletRepository) {
        this.tokenRepository = tokenRepository;
        this.walletRepository = walletRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Token> wallets(@QueryParam("address") String address) {
        if (address != null) {
            Log.infof("Searching for %s", address);
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(tokenRepository.findByAddress(address));
            return tokens;
        }
        return Token.listAll();
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void transfer(String walletAddress) {
        Token t = new Token("name", "symbol", "0x8AC76a51cc950d9822D68b83fE1Ad97B32Cd580d", 18, Network.EthereumLocal);
        Wallet wallet = walletRepository.findByAddress(walletAddress);
        t.persist();
        try {
            RawTransactionAndExtraInfo trx = TransactionGeneration.transferToken(
                    Network.EthereumLocal,
                    t,
                    walletAddress,
                    walletAddress,
                    new BigInteger("156000000000000000000"),
                    null);
            System.out.println(trx.rawTransaction);
            System.out.println(trx.requestedBlockNumber);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Address address = new Address(sAddress);
        // NetworkConfig config = new NetworkConfig();
        // Token token = Token.tokenFromAddress(config, address);
        // Token _token = tokenRepository.findByAddress(token.address);
        // if (_token == null) {
        // }
        // token.persist();
        //
        // return Response.status(Status.CREATED).entity(token).build();

    }

}