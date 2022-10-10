package com.core.models;

public enum TrxReceiptStatus {
    FOUND, // New transaction in block
    CONFIRMED, // After a few blocks
    PARSED, // Connected to Eternal transaction record table
    FAILED, // Transaction Status is 0
    UNKNOWN // Transaction Status is >= 2
}
