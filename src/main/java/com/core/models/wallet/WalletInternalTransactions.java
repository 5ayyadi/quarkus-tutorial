package com.core.models.wallet;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.core.models.Token;
import com.core.models.TransactionStatus;
import com.core.schemas.request.TransferRequest;
import com.google.inject.Inject;
import com.gs.TokenBalanceRepository;
import com.gs.TokenRepository;
import com.gs.WalletInternalTransactionRepository;
import com.gs.WalletRepository;

@Entity
public class WalletInternalTransactions extends WalletTransactionsBasicModel {

    public WalletInternalTransactions() {
    }

    public WalletInternalTransactions(Wallet wallet, String string) {
        super(wallet, string);
    }

    public WalletInternalTransactions(TransferRequest request, TokenRepository tknRepo, WalletRepository wltRepo) {
        this.amount = request.amount;
        this.status = TransactionStatus.SUBMITTED;
        this.type = request.type;
        this.token = tknRepo.findById(request.tokenId);
        this.fromWallet = wltRepo.findByUserId(request.fromUID);
        this.toWallet = wltRepo.findByUserId(request.toUID);
        this.persist();
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
        return balance.compareTo(amount) == 1 ? true : false;
    }

    public int changeStatus(TransactionStatus status, WalletInternalTransactionRepository wltTrxRepo) {
        this.status = status;
        return wltTrxRepo.update("status", status);
    }

    public int changeStatus(TransactionStatus status, String message,  WalletInternalTransactionRepository wltTrxRepo) {
        wltTrxRepo.update("status", status);
        return wltTrxRepo.update("message", message);
    }
    // @Override
    // public boolean transfer(Token token, Wallet toWallet, Decimal amount) {
    // return false;
    // }

}
