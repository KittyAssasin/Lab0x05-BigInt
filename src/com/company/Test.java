package com.company;

import java.math.BigInteger;
import java.util.Random;

public class Test {

    public static void runTest() {
        int n = 1; //n is for arithmetic testing
        int nScale = 2;
        int nSteps = 100; //max n value (nScale^nSteps = n)
        long[] additionTimes = new long[nSteps];
        long[] multiplicationTimes = new long[nSteps];

        int x = 1; //x is for fib testing
        int xInc = 1; //increment value for x
        int xScale = 10; //ev

        System.out.println();
    }

    public static BigInteger getRandom(BigInteger maxValue) {
        Random r = new Random();
        BigInteger rando;
        do {
            rando = new BigInteger(maxValue.bitLength(), r);
        } while (rando.compareTo(maxValue) >= 0);
        return rando;
    }

}
