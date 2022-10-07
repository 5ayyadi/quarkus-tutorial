package com.core.models.wallet;


import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;

import com.core.models.PanacheEntityWithTime;
import com.core.models.Token;
import com.core.network.ERC20;
import com.core.network.Network;
import com.gs.TokenRepository;

@Table(name = "pcm_wallet")
@Entity
public class PCM_Wallet extends PanacheEntityWithTime {

    @Column(updatable = false)
    private String privateKey;

    @Column(updatable = false, unique = true)
    private String publicKey;
    
    @Column(updatable = false, unique = true)
    private Network network;

    public ERC20 contract(String tokenAddress){
        return ERC20.load(tokenAddress, network.value.config.w3, 
        Credentials.create(privateKey), new DefaultGasProvider());
    }

    public String balanceOf(String tokenAddress) {
        ERC20 erc20TokenContract = contract(tokenAddress);
        try {
            return erc20TokenContract.balanceOf(publicKey).send().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean transferTo(String tokenAddress, String dstWallet, BigInteger amount){
        ERC20 contract = contract(tokenAddress);
        try {
            TransactionReceipt receipt = contract.transfer(dstWallet, amount).send();
            receipt.getStatus();
            // TODO: check status
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
