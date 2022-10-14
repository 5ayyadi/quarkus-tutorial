package com.core.schemas.request;

import com.core.network.Network;
import org.web3j.abi.datatypes.Address;

public class TokenRequest {
    @Override
    public String toString() {
        return "TokenRequest [network=" + network + ", address=" + address + "]";
    }

    public Network network;
    public String address;

    public TokenRequest() {
    }

    public TokenRequest(String address, Network network) {
        super();
        this.address = address;
        this.network = network;
    }

    public Address toAddress() {
        if (address != null)
            return new Address(address);
        else
            return null;
    }
}
