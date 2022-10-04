package com.core.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class Decimal extends BigDecimal {

    protected static final RoundingMode roundingMode = RoundingMode.FLOOR;
    protected static final int precision = 77;

    protected static final MathContext DECIMAL_WITH_256_PRECISION = new MathContext(precision, roundingMode);

    // Instances
    public Decimal(BigInteger val) {
        super(val, DECIMAL_WITH_256_PRECISION);
    }

    public Decimal(BigDecimal val) {
        super(val.toString(), DECIMAL_WITH_256_PRECISION);
    }

    public Decimal(String val) {
        super(val, DECIMAL_WITH_256_PRECISION);
    }

    public Decimal(int val) {
        this(new BigInteger(String.format("%d", val)));
    }

    public Decimal(long val) {
        this(Math.toIntExact(val));
    }

    public static final Decimal ONE = new Decimal(1);
    public static final Decimal TWO = new Decimal(2);
    public static final Decimal THREE = new Decimal(3);
    public static final Decimal FOUR = new Decimal(4);
    public static final Decimal FIVE = new Decimal(5);
    public static final Decimal SIX = new Decimal(6);
    public static final Decimal SEVEN = new Decimal(7);
    public static final Decimal EIGHT = new Decimal(8);
    public static final Decimal NINE = new Decimal(9);
    public static final Decimal TEN = new Decimal(10);
    public static final Decimal EIGHTEEN = new Decimal(18);

    // public Dec

    @Override
    public String toString() {
        return super.toString();
    }
}
