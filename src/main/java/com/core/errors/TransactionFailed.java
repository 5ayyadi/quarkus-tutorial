package com.core.errors;

public class TransactionFailed extends Exception {
    String msg;

    public TransactionFailed(String msg) {
        this.msg = msg;
    }
}
