package com.core.schemas.request;

import com.core.customTypes.Address;
import com.core.network.Network;

public class TokenARequest {
    @Override
    public String toString() {
        return "TokenRequest [network=" + network + ", address=" + address + "]";
    }

    public Network network;
    public Address address;

    public TokenARequest() {
    }

    public TokenARequest(Address address, Network network) {
        // super();
        this.address = address;
        this.network = network;
    }

}
