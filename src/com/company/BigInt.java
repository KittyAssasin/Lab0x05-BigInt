package com.company;

public final class BigInt {

    private static final int UNICODE_OFFSET = 48;
    private static final int UNICODE_OFFSET_DOUBLE = UNICODE_OFFSET * 2;

    private final String absValue;
    private final boolean isNegative;
    private final int magnitude;

    public BigInt() {
        this.absValue = "0";
        this.isNegative = false;
        this.magnitude = 0;
    }

    public BigInt(int value) {
        this.isNegative = value < 0;
        if (this.isNegative)
            this.absValue = "" + Math.abs(value);
        else
            this.absValue = "" + value;
        this.magnitude = this.absValue.length() - 1;
    }

    public BigInt(String value) {
        this.isNegative = value.charAt(0) == '-';
        if (isNegative)
            this.absValue = value.substring(1);
        else
            this.absValue = value;
        this.magnitude = this.absValue.length() - 1;
    }

    public BigInt(BigInt bigInt) {
        this.absValue = bigInt.absValue;
        this.isNegative = bigInt.isNegative;
        this.magnitude = bigInt.magnitude;
    }

    public static BigInt negate(BigInt a) {
        if (a.isNegative)
            return new BigInt(a.absValue);
        else
            return new BigInt("-" + a.absValue);
    }

    public static BigInt add(BigInt a, BigInt b) {
        if (isZero(a)) //shortcuts
            return new BigInt(b);
        if (isZero(b))
            return new BigInt(a);

        if (a.isNegative ^ b.isNegative) //if either, but not both, are negative (XOR)
            if (a.isNegative) //if a is negative (-a) + b = b - |a|
                return sub(b, negate(a));
            else
                return sub(a, negate(b)); //if b is negative a + (-b) = a - |b|

        //both are positive or negative, operation is the same on the abs values
        String sum = "";
        boolean carry = false;
        if (a.magnitude >= b.magnitude) { //if a is longer or equal (greater magnitude) than b's, saves time
            int magOffset = a.magnitude - b.magnitude;
            int i;
            for (i = a.magnitude; i - magOffset >= 0; i--) {
                int subSum = a.absValue.charAt(i) + b.absValue.charAt(i - magOffset) - UNICODE_OFFSET_DOUBLE;
                if (carry)
                    subSum++;
                if (subSum > 9) {
                    carry = true;
                    sum = (subSum - 10) + sum;
                } else {
                    carry = false;
                    sum = subSum + sum;
                }
            }
            while (carry) { //if there are left over carry operations (e.q. 9999 + 1) we need to handle
                if (i < 0) { //if we are at the end (e.q. 555 + 555)
                    sum = "1" + sum;
                    break;
                }
                int subSum = a.absValue.charAt(i) + 1 - UNICODE_OFFSET;
                if (subSum < 10) {
                    carry = false;
                    sum = subSum + sum;
                } else {
                    if (i == 0) { //at the highest magnitude
                        carry = false;
                        sum = subSum + sum;
                    } else {
                        sum = (subSum - 10) + sum;
                    }
                }
                i--;
            }
            //getting the rest of numbers
            if (i >= 0)
                sum = a.absValue.substring(0,i+1) + sum;
        } else {
            int magOffset = b.magnitude - a.magnitude;
            int i;
            for (i = b.magnitude; i - magOffset >= 0; i--) {
                int subSum = b.absValue.charAt(i) + a.absValue.charAt(i - magOffset) - UNICODE_OFFSET_DOUBLE;
                if (carry)
                    subSum++;
                if (subSum > 9) {
                    carry = true;
                    sum = (subSum - 10) + sum;
                } else {
                    carry = false;
                    sum = subSum + sum;
                }
            }
            while (carry) { //if there are left over carry operations (e.q. 9999 + 1) we need to handle
                if (i < 0) { //if we are at the end (e.q. 555 + 555)
                    sum = "1" + sum;
                    break;
                }
                int subSum = b.absValue.charAt(i) + 1 - UNICODE_OFFSET;
                if (subSum < 10) {
                    carry = false;
                    sum = subSum + sum;
                } else {
                    if (i == 0) { //at the highest magnitude
                        carry = false;
                        sum = subSum + sum;
                    } else {
                        sum = (subSum - 10) + sum;
                    }
                }
                i--;
            }
            //getting the rest of numbers
            if (i >= 0)
                sum = b.absValue.substring(0,i+1) + sum;
        }
        if (a.isNegative)
            sum = "-" + sum;
        return new BigInt(sum);
    }

    public static BigInt sub(BigInt a, BigInt b) {
        if (isZero(a))
            return negate(b);
        if (isZero(b))
            return new BigInt(a);

        if (a.isNegative ^ b.isNegative)
            return add(a, negate(b));

        if (a.isNegative && b.isNegative) //(-a)-(-b) = b-a
            return sub(negate(b), negate(a));

        //refactored so a and b can only be positive now
        //if (a.magnitude)

        return a;
    }

    public static boolean isZero(BigInt a) {
        return a.absValue.equals("0");
    }

    public static boolean areEqual(BigInt a, BigInt b) {
        return (a.isNegative == b.isNegative) && (a.absValue.equals(b.absValue));
    }

    public static BigInt multiply(BigInt a, BigInt b) {
        return a;
    }

    public String inspect() {
        return "BigInt[absValue:"+absValue+",isNegative:"+isNegative+",magnitude:"+magnitude+"]";
    }

    public String toShortString() {
        if (this.magnitude < 12)
            return this.toString();
        return (isNegative ? "-" : "") + this.absValue.substring(0, 5) + "..." + this.absValue.substring(absValue.length()-5);
    }

    @Override
    public String toString() {
        return (isNegative ? "-" : "") + absValue;
    }
}
