package com.core.schemas.request;

import com.core.network.Network;

public class MasterWalletRequest {
    public Long userId;
    public Network network;
    
    public MasterWalletRequest() {
    }

    public MasterWalletRequest(Long userId, Network network) {
        this.userId = userId;
        this.network = network;
    }

    
}
