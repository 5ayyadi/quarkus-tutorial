package com.core.errors;

import javax.ws.rs.core.Response.Status;

public class BadTransactionHash extends BaseCustomError {

    public String trxHash;

    public BadTransactionHash(String trxHash) {
        this("Bad Transaction Found!", trxHash);
    }

    public BadTransactionHash(String msg, String trxHash) {
        this.errorMsg = msg;
        this.statusCode = Status.BAD_REQUEST;
        this.trxHash = trxHash;
    }
}
