package com.core.models;

public enum TransactionStatus {
    RECEIVED("RECEIVED"), // when the request gets to server
    PENDING("PENDING"), // when the transaction is being proccessed
    CONFIRMED("CONFIRMED"), // state of when the trx is done and must change in centralized side
    SUCCESS("SUCCESS"),
    REVERTED("REVERTED");

    private String status;

    TransactionStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
