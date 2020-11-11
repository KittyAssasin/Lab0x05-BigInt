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

    public BigInt negate() {
        if (this.isNegative)
            return new BigInt(this.absValue);
        else
            return new BigInt("-" + this.absValue);
    }

    public BigInt add(BigInt b) {
        if (this.isZero()) //shortcuts
            return new BigInt(b);
        if (b.isZero())
            return new BigInt(this);

        if (this.isNegative ^ b.isNegative) //if either, but not both, are negative (XOR)
            if (this.isNegative) //if a is negative (-a) + b = b - |a|
                return b.sub(this.negate());
            else
                return this.sub(b.negate()); //if b is negative a + (-b) = a - |b|

        //both are positive or negative, operation on abs value is the same
        BigInt A;
        BigInt B;
        if (this.magnitude >= b.magnitude) { //checks for longer value
            A = this;
            B = b;
        } else {
            A = b;
            B = this;
        }

        String sum = "";
        boolean carryFlag = false;
        int magOffset = A.magnitude - B.magnitude;
        int i;
        for (i = A.magnitude; i - magOffset >= 0; i--) {
            int subSum = A.absValue.charAt(i) + B.absValue.charAt(i - magOffset) - UNICODE_OFFSET_DOUBLE;
            if (carryFlag)
                subSum++;
            if (subSum > 9) {
                carryFlag = true;
                sum = (subSum - 10) + sum;
            } else {
                carryFlag = false;
                sum = subSum + sum;
            }
        }
        while (carryFlag) { //if there are left over carry operations (e.q. 9999 + 1) we need to handle
            if (i < 0) { //if we are at the end (e.q. 555 + 555)
                sum = "1" + sum;
                break;
            }
            int subSum = A.absValue.charAt(i) + 1 - UNICODE_OFFSET;
            if (subSum < 10) {
                carryFlag = false;
                sum = subSum + sum;
            } else {
                if (i == 0) { //at the highest magnitude
                    carryFlag = false;
                    sum = subSum + sum;
                } else {
                    sum = (subSum - 10) + sum;
                }
            }
            i--;
        }
        //getting the rest of numbers
        if (i >= 0)
            sum = A.absValue.substring(0,i + 1) + sum;

        return new BigInt((this.isNegative ? "-" : "") + sum);
    }

    public BigInt sub(BigInt b) {
        if (this.isZero())
            return b.negate();
        if (b.isZero())
            return new BigInt(this);

        if (this.isEqual(b))
            return new BigInt(0);

        if (this.isNegative ^ b.isNegative)
            return this.add(b.negate());

        if (this.isNegative && b.isNegative) //(-a)-(-b) = b-a
            return b.negate().sub(this.negate());


        //refactored so a and b can only be positive now
        boolean negate = !this.isGreater(b);
        BigInt A;
        BigInt B;
        if (negate) { // A always > B
            A = b;
            B = this;
        } else {
            A = this;
            B = b;
        }
        String diff = "";
        int magOffset = A.magnitude - B.magnitude;
        boolean carryFlag = false;
        int i;
        for (i = A.magnitude; i - magOffset>= 0; i--) {
            int subDiff = A.absValue.charAt(i) - B.absValue.charAt(i - magOffset); // no double offset needed
            if (carryFlag)
                subDiff--;
            if (subDiff < 0) {
                carryFlag = true;
                diff = (subDiff + 10) + diff;
            } else {
                carryFlag = false;
                diff = subDiff + diff;
            }
        }
        while (carryFlag) {
            if (i < 0)
                break;
            int subDiff = A.absValue.charAt(i) - 1 - UNICODE_OFFSET;
            if (subDiff >= 0) {
                diff = subDiff + diff;
                carryFlag = false;
            } else {
                diff = (subDiff + 10) + diff;
            }
            i--;
        }
        diff = trimLeadingZeros(diff);
        return new BigInt((negate ? "-" : "") + diff);
    }

    public boolean isZero() {
        return this.absValue.equals("0");
    }

    public boolean isEqual(BigInt b) {
        return (this.isNegative == b.isNegative) && (this.absValue.equals(b.absValue));
    }

    public boolean isGreater(BigInt b) {

        if (this.isEqual(b))
            return false;

        if (!this.isNegative) {
            if (b.isNegative) // positive a > negative b
                return true;

            //positive a ?> positive b
            if (this.magnitude > b.magnitude) // pos aa > pos b
                return true;
            if (this.magnitude < b.magnitude) // pos a < pos bb
                return false;

            for (int i = 0; i <= this.magnitude; i++) {
                if (this.absValue.charAt(i) > b.absValue.charAt(i))
                    return true;
                if (this.absValue.charAt(i) < b.absValue.charAt(i))
                    return false;
                // continue if elements at i are the same
            }
        } else {
            if (!b.isNegative) // negative a < positive b
                return false;

            //negative a ?> negative b
            if (this.magnitude > b.magnitude) // neg aa < neg b
                return false;
            if (this.magnitude < b.magnitude) //neg a > neg bb
                return true;

            for (int i = 0; i <= this.magnitude; i++) {
                if (this.absValue.charAt(i) > b.absValue.charAt(i))
                    return false;
                if (this.absValue.charAt(i) < b.absValue.charAt(i))
                    return false;
                // continue if elements at i are the same
            }
        }
        return false; // safeguard, numbers are equal
    }

    private static String trimLeadingZeros(String s) {
        if (s.charAt(0) == '0')
            return trimLeadingZeros(s.substring(1));
        return s;
    }

    public BigInt multiply(BigInt b) {
        return this;
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
