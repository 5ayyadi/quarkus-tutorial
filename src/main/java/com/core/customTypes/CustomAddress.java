package com.core.customTypes;

import com.core.customTypes.Address;

/**
 * Address type, which is equivalent to uint160.
 */
public class CustomAddress extends Address {

    public CustomAddress(String hexValue) {
        super(hexValue.substring(0, 2).equals("0x") ? hexValue : "0x" + hexValue);
    }
}
