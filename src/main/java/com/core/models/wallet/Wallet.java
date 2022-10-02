package com.core.models.wallet;

// import java.sql.Date;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.core.models.PanacheEntityWithTime;
import com.core.models.TokenBalances;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.core.wallet.WalletKeyPair;
import com.core.wallet.HDWallet;
import com.core.math.Decimal;
import com.core.network.Network;

@Table(name = "wallet")
@Entity
public class Wallet extends PanacheEntityWithTime {

    // TODO: add setter to this class

    // public int id;

    @Column(updatable = false, unique = true)
    public String address;

    @Column(unique = true)
    public long userId;

    @Column(updatable = false)
    private String privateKey;

    @Column(updatable = false, unique = true)
    private String publicKey;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<TokenBalances> tokenBalances = new HashSet<TokenBalances>();

    // Amount of Network Value in the wallet (Transfer Only)
    @Column(length = 80)
    private String valueBalance;

    @Column
    @CreationTimestamp
    private Date createdAt;

    @Column
    @UpdateTimestamp
    private Date modifiedAt;

    @Override
    public void persist() {
        // TODO - Make Sure UserIds are long and not overflowing
        this.populateKeyPair(this.userId);
        this.address = this.publicKey;
        if (this.valueBalance == null) {
            this.valueBalance = "0";
        }
        super.persist();
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = new Date();
            System.out.println(createdAt);
        }
        System.out.println(this.id);
    }

    // @PreUpdate

    public String getValueBalance() {
        return valueBalance;
    }

    public void setValueBalance(String valueBalance) {
        this.valueBalance = valueBalance;
    }

    public Set<TokenBalances> getTokenBalances() {
        return tokenBalances;
    }

    public void addTokenBalance(TokenBalances tb) {
        tokenBalances.add(tb);
    }

    public void populateKeyPair(Long accountIdentifier) {
        if (privateKey == null) {
            try {
                WalletKeyPair keyPair = HDWallet.Create(Network.Ethereum, Math.toIntExact(accountIdentifier));
                this.privateKey = keyPair.getPrivateKeyString();
                this.publicKey = keyPair.getPublicKeyString();
            } catch (UnreadableWalletException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getAddress() {
        return this.address;
    }

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

}
