package com.core.wallet;

import java.math.BigInteger;

import org.web3j.crypto.ECKeyPair;

public class WalletKeyPair extends ECKeyPair {

    public WalletKeyPair(String privateKey, String publicKey) {
        this(new BigInteger(publicKey, 16), new BigInteger(privateKey, 16));
    }

    public WalletKeyPair(BigInteger privateKey, BigInteger publicKey) {
        super(publicKey, privateKey);
    }

    @Override
    public String toString() {
        return "WalletKeyPair{" +
                "privateKey=" + getPrivateKey() +
                ", publicKey=" + getPublicKey() +
                '}';
    }

    public String getPrivateKeyString() {
        return getPrivateKey().toString(16);
    }

    public String getPublicKeyString() {
        return getPublicKey().toString(16);
    }

    // TODO - How does override works ????
    @Override
    public BigInteger getPrivateKey() {
        // TODO Auto-generated method stub
        return super.getPrivateKey();
    }

    @Override
    public BigInteger getPublicKey() {
        // TODO Auto-generated method stub
        return super.getPublicKey();
    }
}
