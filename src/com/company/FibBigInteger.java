package com.company;

import java.math.BigInteger;

public class FibBigInteger {

    //classic fib with BigInt
    public static BigInteger fibLoop(int nth) {
        BigInteger a = new BigInteger("0");
        BigInteger b = new BigInteger("1");
        BigInteger c;

        if (nth == 0)
            return a;

        for (int i = 2; i <= nth; i++) {
            c = a.add(b);
            a = b;
            b = c;
        }

        return b;
    }

    //fibMatrix with BigInt
    public static BigInteger fibMatrix(int nth) {
        if (nth == 0)
            return new BigInteger("0");

        int[][] init = {{1,1},{1,0}};
        MatrixBigInteger m = new MatrixBigInteger(init);

        m = m.power(nth - 1);

        return m.get(0,0);
    }
}
