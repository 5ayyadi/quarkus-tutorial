package com.core.schemas.request;

import com.core.customTypes.Address;

public class AddressRequest {
    public String address;

    // TODO - Add checksum validator
    public AddressRequest() {
    }

    public AddressRequest(String addressString) {
        this.address = addressString;
    }

    public AddressRequest(Address address) {
        this.address = address.toString();
    }

    public Address getAddress() {
        return new Address(address);
    }
}
