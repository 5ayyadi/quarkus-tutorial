package com.core.errors;

import javax.ws.rs.core.Response.Status;

public class TransactionFailed extends BaseCustomError {

    public TransactionFailed(String msg) {
        this.errorMsg = msg;
        this.statusCode = Status.EXPECTATION_FAILED;
    }
}
