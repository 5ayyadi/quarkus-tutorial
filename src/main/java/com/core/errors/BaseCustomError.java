package com.core.errors;

import javax.ws.rs.core.Response.Status;

public class BaseCustomError extends RuntimeException {
    public String errorMsg;

    public Status statusCode;

    public BaseCustomError() {
    }

    public BaseCustomError(String errorMsg, Status statusCode) {
        this.errorMsg = errorMsg;
        this.statusCode = statusCode;

    }

    public BaseCustomError(String errorMsg) {
        this(errorMsg, 0);
    }

    public BaseCustomError(String errorMsg, int statusCode) {
        this(errorMsg, Status.fromStatusCode(statusCode));
    }

    @Override
    public String getMessage() {
        return String.format("[%s]::[%s]", statusCode, errorMsg);
    }

    @Override
    public String toString() {
        return "{\"error\":\"" + errorMsg + "\"}";
    }

}
