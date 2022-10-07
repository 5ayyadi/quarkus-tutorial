package com.core.schemas.request;

public class WalletCreationRequest {
    public Long userId;

    public WalletCreationRequest() {
    }

    public WalletCreationRequest(Long userId) {
        this.userId = userId;
    }
}
