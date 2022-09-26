package com.core.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.bitcoinj.wallet.UnreadableWalletException;

import com.core.wallet.WalletKeyPair;
import com.core.wallet.HDWallet;
import com.core.math.Decimal;
import com.core.network.Network;

@Entity
public class Wallet extends PanacheEntity {

    // TODO: add setter to this class

    // public int id;

    @Column(updatable = false, unique = true)
    public String address;

    @Column(unique = true)
    public long userId;

    @Column(updatable = false)
    public String privateKey;

    @Column(updatable = false, unique = true)
    public String publicKey;

    // Amount of Network Value in the wallet (Transfer Only)
    @Column(length = 80)
    public String ValueBalance;

    @Override
    public void persist() {
        // TODO - Make Sure UserIds are long and not overflowing
        this.updateKeys();
        this.address = this.publicKey;
        super.persist();
    }

    // public Decimal getValueBalance
    public Decimal getValueBalance() {
        return new Decimal(ValueBalance);
    }

    public void setValueBalance(Decimal valueBalance) {
        ValueBalance = valueBalance.toString();
    }

    public void updateKeys() {
        if (privateKey == null) {
            try {
                WalletKeyPair keyPair = HDWallet.Create(Network.Ethereum, Math.toIntExact(this.userId));
                this.privateKey = keyPair.getPrivateKeyString();
                this.publicKey = keyPair.getPrivateKeyString();
            } catch (UnreadableWalletException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getAddress() {
        return this.address;
    }

    // TODO - Token based Balance another table
    // public Long getBalance(TokenAddress address) {
    // return this.balance;
    // }

    @Override
    public String toString() {
        return "Wallet{" +
                "address='" + address + '\'' +
                ", userId=" + userId +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", balance=" + getValueBalance() +
                ", id=" + id +
                '}';
    }

    public static List<Wallet> findByAddress(String address) {
        return find("address", address).list();
    }

    public static Wallet findByUserId(long userId) {
        return find("userId", userId).firstResult();
    }

}
