package com.core.models.wallet;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.core.models.Token;
import com.core.models.TransactionStatus;
import com.core.models.TransactionType;
import com.core.schemas.request.TransferRequest;
import com.core.repositories.TokenBalanceRepository;
import com.core.repositories.TokenRepository;
import com.core.repositories.WalletInternalTransactionRepository;
import com.core.repositories.WalletRepository;

@Entity
@Table(name = "wallet_in_trx")
public class WalletInternalTransactions extends WalletTransactionsBasicModel {

    @OneToOne
    @JoinColumn(name = "wallet_ex_trx_id", nullable = true)
    // @MapsId
    // @Column(nullable = true)
    private WalletExternalTransactions boundedExternalTrx;

    public WalletInternalTransactions() {
    }

    public WalletInternalTransactions(Token token, Wallet fromWallet, Wallet toWallet, BigInteger amount) {
        this();
        this.fromWallet = fromWallet;
        this.toWallet = toWallet;
        this.toWallet = toWallet;
        this.amount = amount;
        this.token = token;
        this.status = TransactionStatus.RECEIVED;
        this.type = TransactionType.TRANSFER;
    }

    public WalletInternalTransactions(TransferRequest request, TokenRepository tknRepo, WalletRepository wltRepo) {
        this.amount = request.amount;
        this.status = TransactionStatus.SUBMITTED;
        this.type = request.type;
        this.token = tknRepo.findById(request.tokenId);
        this.fromWallet = wltRepo.findByUserId(request.fromUID);
        this.toWallet = wltRepo.findByUserId(request.toUID);

    }

    @Override
    public boolean transfer(Token token, Wallet toWallet, BigInteger amount) {
        // TODO Auto-generated method stub
        return false;
    }

    public static List<WalletInternalTransactions> findByStatus(TransactionStatus status) {
        return list("status", status);
    }

    public boolean isTransferValid(TokenBalanceRepository tbRepo) {
        // TODO: later on we can add nonce or avoid duplication
        return _hasBalance(tbRepo);
    }

    private boolean _hasBalance(TokenBalanceRepository tbRepo) {
        BigInteger balance = new BigInteger(tbRepo.getTokenBalance(fromWallet.id, token.id));
        return balance.compareTo(amount) == 1;
    }

    public int changeStatus(TransactionStatus status, WalletInternalTransactionRepository wltTrxRepo) {
        this.status = status;
        return wltTrxRepo.update("status", status);
    }

    public int changeStatus(TransactionStatus status, String message, WalletInternalTransactionRepository wltTrxRepo) {
        wltTrxRepo.update("status", status);
        return wltTrxRepo.update("message", message);
    }

    public static WalletInternalTransactions fromDepositExtInternalTransactions(
            WalletExternalTransactions externalTrx) {
        return fromExtInternalTransactions(externalTrx, TransactionType.DEPOSIT);
    }

    public static WalletInternalTransactions fromWithdrawExtInternalTransactions(
            WalletExternalTransactions externalTrx) {
        return fromExtInternalTransactions(externalTrx, TransactionType.WITHDRAW);
    }

    private static WalletInternalTransactions fromExtInternalTransactions(WalletExternalTransactions externalTrx,
            TransactionType transactionType) {
        WalletInternalTransactions internalTrx = new WalletInternalTransactions();
        internalTrx.fromWallet = externalTrx.fromWallet;
        internalTrx.status = TransactionStatus.FOUND;
        internalTrx.toWallet = externalTrx.toWallet;
        internalTrx.amount = externalTrx.amount;
        internalTrx.token = externalTrx.token;
        internalTrx.type = transactionType;
        internalTrx.boundedExternalTrx = externalTrx;
        return internalTrx;
    }
}
