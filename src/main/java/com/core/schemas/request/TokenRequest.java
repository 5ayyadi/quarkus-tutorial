package com.core.schemas.request;

import com.core.network.Network;
import org.web3j.abi.datatypes.Address;

public class TokenRequest {
    public Network network;
    public String address;

    public TokenRequest() {
    }

    public TokenRequest(String address, Network network) {
        super();
        this.address = address;
        this.network = network;
    }

    public Address getAddress() {
        return new Address(address);
    }
}
