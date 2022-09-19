package com.gs;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.core.KeyPair;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import com.core.WalletUtils;
import io.quarkus.hibernate.orm.panache.runtime.JpaOperations;
import org.bitcoinj.wallet.UnreadableWalletException;

@Entity
public class Wallet extends PanacheEntity {

    // TODO: add setter to this class

    public String address;

//    @Column(unique = true)
    public int userId;

//    @Column(updatable = false)
    public String privateKey;

//    @Column(updatable = false, unique = true)
    public String publicKey;

    public Long balance;

    @Override
    public void persist() {
        this.updateKeys();
        this.address = this.publicKey;
        super.persist();
    }

    public void updateKeys() {
        if (privateKey == null){
            try {
                KeyPair keyPair = WalletUtils.Create(this.userId);
                this.privateKey = (String) keyPair.privateKey;
                this.publicKey = (String) keyPair.pubKey;
                System.out.println(keyPair);
            } catch (UnreadableWalletException e) {
                throw new RuntimeException(e);
            }
        }
    }



    public String getAddress() {
        return this.address;
    }

    public Long getBalance() {
        return this.balance;
    }

    public static List<Wallet> findByAddress(String address) {
        return find("address", address).list();
    }
    public static Wallet findByUserId(int userId) {
        return find("userId", userId).firstResult();
    }

}
