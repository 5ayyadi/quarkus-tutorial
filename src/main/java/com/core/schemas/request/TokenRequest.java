package com.core.schemas.request;

import com.core.network.Network;
import com.core.customTypes.Address;

public class TokenRequest {
    @Override
    public String toString() {
        return "TokenRequest [network=" + network + ", address=" + address + "]";
    }

    public Network network;
    public Address address;

    public TokenRequest() {
    }

    public TokenRequest(Address address, Network network) {
        super();
        this.address = address;
        this.network = network;
    }

    // public Address toAddress() {
    // if (address != null)
    // return new Address(address);
    // else
    // return null;
    // }
}
