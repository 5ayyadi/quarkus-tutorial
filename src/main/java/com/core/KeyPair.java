package com.core;

public class KeyPair<A, B> {
    public final A privateKey;
    public final B pubKey;

    public KeyPair(A privateKey, B pubKey) {
        this.privateKey = privateKey;
        this.pubKey = pubKey;
    }

    @Override
    public String toString() {
        return "KeyPair{" +
                "privateKey=" + privateKey +
                ", pubKey=" + pubKey +
                '}';
    }
}
