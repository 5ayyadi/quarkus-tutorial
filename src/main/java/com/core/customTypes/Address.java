package com.core.customTypes;

import java.math.BigInteger;
import java.util.Objects;

import javax.persistence.Transient;

import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Uint160;
import org.web3j.utils.Numeric;

interface Type<T> {
    int MAX_BIT_LENGTH = 256;
    int MAX_BYTE_LENGTH = MAX_BIT_LENGTH / 8;

}

/**
 * Address type, which is equivalent to uint160.
 */
public class Address implements Type<String> {

    public static final int LENGTH = 160;
    public static final String TYPE_NAME = "address";
    public static final int LENGTH_IN_HEX = LENGTH >> 2;
    public static final Address ZERO = new Address(BigInteger.ZERO);
    public static final Address DEFAULT = new Address(BigInteger.ZERO);
    public static final Address VALUE_TOKEN_ADDRESS = new Address("0xEeeeeEeeeEeEeeEeEeEeeEEEeeeeEeeeeeeeEEeE");

    private String value;

    public Address() {

    }

    public Address(BigInteger value) {
        this.value = Numeric.toHexStringWithPrefixZeroPadded(value, LENGTH_IN_HEX);
    }

    public Address(String hexValue) {
        this(Numeric.toBigInt(hexValue));
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = validator(value);
    }

    private static String validator(String value) {
        return Numeric.toHexStringWithPrefixZeroPadded(Numeric.toBigInt(value), LENGTH_IN_HEX);
    }

    public Uint160 toUint160() {
        return new Uint160(new BigInteger(value));
    }

    public BigInteger toBigInteger() {
        return Numeric.toBigInt(value);
    }

    @Override
    public String toString() {
        return value;
    }

    public String toPublicKey() {
        return value.substring(2, 42);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Address address = (Address) o;

        return Objects.equals(value, address.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
