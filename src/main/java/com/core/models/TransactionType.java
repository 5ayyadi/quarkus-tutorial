package com.core.models;

public enum TransactionType {
    TRANSFER("TRANSFER"),
    DEPOSIT("DEPOSIT"),
    WITHDRAW("WITHDRAW"),
    CONTRACT_CALL("CONTRACT_CALL"),
    ERC20TRANSFER("ERC20TRANSFER"),
    ETH_TRANSFER("ETH_TRANSFER"),
    UNKNOWN("UNKNOWN");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
