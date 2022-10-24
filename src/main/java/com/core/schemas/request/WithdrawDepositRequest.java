package com.core.schemas.request;

import java.math.BigInteger;

import com.core.customTypes.Address;

import com.core.models.TransactionStatus;
import com.core.models.wallet.WalletExternalTransactions;
import com.core.network.Network;

public class WithdrawDepositRequest extends TokenBalancesRequest {
    public Long userId;
    public Long tokenId = null;
    public TransactionStatus status = TransactionStatus.RECEIVED;
    public Address userWallet;
    public String trxHash;

    public WithdrawDepositRequest() {
    }

    public WithdrawDepositRequest(
            Long userId,
            Address serverWallet,
            Address userWallet,
            Address tokenAddress,
            BigInteger amount,
            Network network, String trxHash) {
        this.userId = userId;
        this.serverWallet = serverWallet;
        this.userWallet = userWallet;
        this.tokenAddress = tokenAddress;
        this.amount = amount;
        this.network = network;
        this.trxHash = trxHash;
    }

    public void changeStatus(TransactionStatus status) {
        this.status = status;
    }

    public static WithdrawDepositRequest toWithdrawDepositRequest(WalletExternalTransactions wet) {
        // TODO - PLease Fill this bit ...

        // new WithdrawDepositRequest(null, null, null, null, null, null, null)
        // trxReceipt.transactionHash,
        // new Address(wet.token.address),
        // wet.amount,
        // network,
        // wet.toWallet
        return null;
    }

}
