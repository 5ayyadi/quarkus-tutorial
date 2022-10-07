package com.core.errors;

import javax.ws.rs.core.Response.Status;

public class ReachedMaxUserId extends BaseCustomError {
    public static final int MAX_USER_ID = 2147483647;

    String msg;

    public ReachedMaxUserId() {
        this.errorMsg = String.format("Can Only support %s users ...", MAX_USER_ID);
        this.statusCode = Status.BAD_REQUEST;

    }
}
