package com.core.customTypes;

import org.web3j.abi.datatypes.Address;

/**
 * Address type, which is equivalent to uint160.
 */
public class CustomAddress {

    public String address;

    public CustomAddress(String hexValue) {
        this.address = hexValue.startsWith("0x") ? hexValue : "0x" + hexValue;
    }

    public static Address toAddress(String hexValue) {
        String address = hexValue.startsWith("0x") ? hexValue : "0x" + hexValue;
        return new Address(address);
    }
}
