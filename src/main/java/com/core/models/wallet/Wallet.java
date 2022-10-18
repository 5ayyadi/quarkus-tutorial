package com.core.models.wallet;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.bitcoinj.wallet.UnreadableWalletException;
import com.core.customTypes.Address;

import com.core.errors.ReachedMaxUserId;
import com.core.models.PanacheEntityWithTime;
import com.core.models.Token;
import com.core.models.TokenBalances;
import com.core.models.TransactionStatus;
import com.core.network.Network;
import com.core.schemas.request.TransferRequest;
import com.core.schemas.request.WithdrawDepositRequest;
import com.core.wallet.HDWallet;
import com.core.wallet.WalletKeyPair;
import com.gs.TokenBalanceRepository;
import com.gs.TokenRepository;
import com.gs.WalletRepository;

@Table(name = "wallet")
@Entity
public class Wallet extends PanacheEntityWithTime {

    // TODO: add setter to this class

    // public int id;

    @Column(updatable = false, unique = true, columnDefinition = "varchar")
    private String address;

    @Column(unique = true)
    public long userId;

    @Column(updatable = false)
    private String privateKey;

    @Column(updatable = false, unique = true)
    private String publicKey;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<TokenBalances> tokenBalances = new HashSet<TokenBalances>();

    // Amount of Network Value in the wallet (Transfer Only)
    // @Column(precision = 100, scale = 0, nullable = true)
    @Column(length = 80)
    @Transient
    private String valueBalance;

    public Wallet() {
    }

    public Wallet(Long userId) {
        this.userId = userId;
    }

    @PrePersist
    public void populateKeysUsingUID() throws ReachedMaxUserId {
        // TODO - Make Sure UserIds are long and not overflowing
        if (this.userId > ReachedMaxUserId.MAX_USER_ID) {
            throw new ReachedMaxUserId();
        }
        ;
        this.populateKeyPair(this.userId);
        this.address = (new Address(this.publicKey)).toString();
        if (this.valueBalance == null) {
            this.valueBalance = "0";
        }
    }

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

    public Address getAddress() {
        return new Address(address);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public TokenBalances getTokenBalances(Token token) {
        Set<TokenBalances> allBalances = this.getTokenBalances();
        for (TokenBalances tb : allBalances) {
            if (tb.getToken().equals(token)) {
                return tb;
            }
        }
        return null;
    }

    public void updateTokenBalances(Token token, String balance) {
        TokenBalances tb = this.getTokenBalances(token);
        tb.setBalance(balance);
        tb.persist();
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

    public void deposit(WithdrawDepositRequest request, TokenBalanceRepository tbRepo, TokenRepository tknRepo,
            WalletRepository wltRepo) {
        String balanceString = tbRepo.getTokenBalance(this.id, request.tokenId);
        if (balanceString == null) {
            TokenBalanceRepository.addTokenBalances(request, tknRepo, wltRepo);
        } else {
            BigInteger balance = new BigInteger(balanceString);
            balance = balance.add(request.amount);
            tbRepo.changeTokenBalance(this.id, request.tokenId, balance.toString());
            request.changeStatus(TransactionStatus.SUCCESS);
        }
    }

    public void withdraw(WithdrawDepositRequest request, TokenBalanceRepository tbRepo) {
        BigInteger balance = new BigInteger(tbRepo.getTokenBalance(this.id, request.tokenId));
        balance = balance.subtract(request.amount);
        tbRepo.changeTokenBalance(this.id, request.tokenId, balance.toString());
        request.changeStatus(TransactionStatus.SUCCESS);
    }

    public void deposit(TransferRequest request, TokenBalanceRepository tbRepo) {
        BigInteger balance = new BigInteger(tbRepo.getTokenBalance(this.id, request.tokenId));
        balance = balance.add(request.amount);
        tbRepo.changeTokenBalance(this.id, request.tokenId, balance.toString());
        // TODO: add token repository here and in withdraw
    }

    public void withdraw(TransferRequest request, TokenBalanceRepository tbRepo) {
        BigInteger balance = new BigInteger(tbRepo.getTokenBalance(this.id, request.tokenId));
        balance = balance.subtract(request.amount);
        tbRepo.changeTokenBalance(this.id, request.tokenId, balance.toString());
    }

    public boolean hasBalance(WithdrawDepositRequest request, TokenBalanceRepository tbRepo) {
        BigInteger balance = new BigInteger(
                tbRepo.getTokenBalance(this.id, request.tokenId));
        // returns 0 if equals and -1 if balance is less than amount
        return balance.compareTo(request.amount) == 1 ? true : false;
    }

    public boolean hasBalance(TransferRequest request, TokenBalanceRepository tbRepo) {
        BigInteger balance = new BigInteger(tbRepo.getTokenBalance(this.id, request.tokenId));
        // returns 0 if equals and -1 if balance is less than amount
        return balance.compareTo(request.amount) == 1 ? true : false;
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
