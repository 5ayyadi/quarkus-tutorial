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
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;

import com.core.customTypes.Address;

import com.core.errors.ReachedMaxUserId;
import com.core.models.PanacheEntityWithTime;
import com.core.models.Token;
import com.core.models.TokenBalances;
import com.core.models.TransactionStatus;
import com.core.network.ERC20;
import com.core.network.Network;
import com.core.schemas.request.TransferRequest;
import com.core.schemas.request.WithdrawDepositRequest;
import com.core.wallet.HDWallet;
import com.core.wallet.WalletKeyPair;
import com.core.repositories.TokenBalanceRepository;
import com.core.repositories.TokenRepository;
import com.core.repositories.WalletRepository;

import io.quarkus.logging.Log;

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

    // TODO Impelement Functionality
    boolean isEnabled = true;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final Set<TokenBalances> tokenBalances = new HashSet<TokenBalances>();

    public static String getPublicKeyInHex(String privateKeyInHex) {
        BigInteger privateKeyInBT = new BigInteger(privateKeyInHex, 16);
        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);
        BigInteger publicKeyInBT = aPair.getPublicKey();
        String sPublickeyInHex = publicKeyInBT.toString(16);
        return sPublickeyInHex;
    }

    public Wallet() {
    }

    public Wallet(Long userId) {
        this.userId = userId;
    }

    @PrePersist
    public void populateKeysUsingUID() throws ReachedMaxUserId {
        if (this.userId > ReachedMaxUserId.MAX_USER_ID) {
            throw new ReachedMaxUserId();
        }
        this.populateKeyPair(this.userId);
        this.address = (new Address(this.publicKey)).toString();

    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
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
        return balance.compareTo(request.amount) == 1;
    }

    public boolean hasBalance(TransferRequest request, TokenBalanceRepository tbRepo) {
        BigInteger balance = new BigInteger(tbRepo.getTokenBalance(this.id, request.tokenId));
        // returns 0 if equals and -1 if balance is less than amount
        return balance.compareTo(request.amount) == 1;
    }

    public boolean externalTransfer(
            Address destination,
            Token token,
            BigInteger amount,
            Network network) {
        Credentials credentials = Credentials.create(this.privateKey);
        // Why didn't I used token.tokenContract? because
        // the private key may vary. :(
        ERC20 tokenContract = ERC20.load(
                token.getAddress().toString(),
                network.value.w3,
                credentials,
                new DefaultGasProvider());

        try {

            // ======================================================
            // gas is returned, only needed to sign
            // ======================================================
            // String hexAmount = String.format("%064x", amount);
            // String data = "0xa9059cbb000000000000000000000000" +
            // destination.toPublicKey() +
            // hexAmount;
            // Transaction transaction = new Transaction(
            // this.address,
            // network.value.w3.ethGetTransactionCount(this.address,
            // DefaultBlockParameterName.LATEST).send().getTransactionCount(),
            // network.w3.ethGasPrice().send().getGasPrice(),
            // new BigInteger("210000"),
            // destination.toString(),
            // amount,
            // data);
            // EthEstimateGas gas = network.value.w3.ethEstimateGas(transaction).send();
            // =====================================================

            // This returns error
            TransactionReceipt trxReceipt = tokenContract.transfer(destination.toString(), amount).sendAsync().get();
            return trxReceipt.isStatusOK();
        } catch (Exception e) {
            Log.errorf("from: %s error: %s", this.address, e);
            return false;
        }
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "address='" + address + '\'' +
                ", userId=" + userId +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", id=" + id +
                '}';
    }

}
