package com.core.models;

public enum TransactionType {
    TRANSFER("TRANSFER"),
    DEPOSIT("DEPOSIT"), 
    WITHDRAW("WITHDRAW");

    private String value;
 
    TransactionType(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
    
}
