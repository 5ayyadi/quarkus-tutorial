package com.core.models.wallet;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import com.core.customTypes.Address;
import com.core.models.PanacheEntityWithTime;
import com.core.models.Token;
import com.core.models.TrxReceipt;
import com.core.network.ERC20;
import com.core.network.Network;
import com.core.network.SendTransaction;
import com.core.network.TransactionGeneration;
import com.core.network.TransactionGeneration.RawTransactionAndExtraInfo;

// don't comment @Entity :(
@Entity
public class MasterWallet extends Wallet {

        // @OneToOne
        // @JoinColumn(name = "wallet_id")
        // private Wallet wallet;

        public MasterWallet(Long userId) {
                super(userId);
        }

        public MasterWallet() {
        }

        public TransactionReceipt transferTo(Network network, Token token, Address dstWallet, BigInteger amount)
                        throws Exception {
                // ERC20 contract = token.tokenContract();
                // Use TrxHash as data ...
                RawTransactionAndExtraInfo rxi = TransactionGeneration.transferToken(network, token,
                                this.getAddress().toString(),
                                dstWallet.toString(), amount);
                TrxReceipt trxReceipt = SendTransaction.SignAndSend(network, rxi.rawTransaction, this.getPrivateKey(),
                                this.getPublicKey());
                trxReceipt.persist();
                return null;
        }

        public boolean transferTo(Network network,
                        Token token, String destination, BigInteger amount) throws Exception {
                byte chainId = network.value.chainId.byteValue();
                RawTransactionManager tokenTxManager = new RawTransactionManager(
                                network.w3, Credentials.create(this.getPrivateKey()), chainId);
                ERC20 tokenContract = ERC20.load(token.getAddress().toString(), network.w3, tokenTxManager,
                                new DefaultGasProvider());
                TransactionReceipt receipt = tokenContract.transfer(destination, amount).send();
                return receipt.isStatusOK();
        }

}
