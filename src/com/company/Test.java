package com.company;

import java.math.BigInteger;
import java.util.Random;

public class Test {

    public static void runTest(boolean verbose) {
        final long MAX_TIME = 300_000_000_000L;
        final int MAX_TRIALS = 10;
        long startTime;

        int n; //n is for arithmetic testing
        int nStart = 1;
        int nScale = 2;
        int nSteps = 23; //max n value (nScale^nSteps = n)
        long[] additionTimes = new long[nSteps];
        long[] multiplicationTimes = new long[nSteps];
        //for performance reasons, I will not be logging the generated values, as it's simply fluff

        int x; //x is for fib testing
        int xStart = 1;
        int xInc; //increment value for x, gets multiplied by xScale every 9 xIncs of x 1-9, 10...90, 100...900
        int xIncStart = 1;
        int xScale = 10;
        int xNSteps = 5;
        long[][] fibloopTimes = new long[xScale-1][xNSteps];
        long[][] fibmatrixTimes = new long[xScale-1][xNSteps];

        //Initial printing
        System.out.println("Environment Settings:\nMax Time = " + MAX_TIME + ", Max Trials = " + MAX_TRIALS);
        System.out.println("Arithmetic Settings:\nStart = " + nStart + ", Scale = " + nScale + ", Steps = " + nSteps);
        System.out.println("Fib Settings:\nStart = " + xStart + ", Scale = " + xScale + ", Steps = " + xNSteps);

        //arithmetic testing
        n = nStart;
        for (int i = 0; i < nSteps; i++) {
            long totalTime = 0L;
            int numTrials = 0;

            if (verbose)
                System.out.println("Addition[" + i + "/" + nSteps + ", n=" + n + "]");

            for (; numTrials < MAX_TRIALS; numTrials++) {
                BigInteger A = getRandom(n);
                BigInteger B = getRandom(n);

                startTime = System.nanoTime();
                A.add(B);
                totalTime += System.nanoTime() - startTime;

                if (totalTime > MAX_TIME)
                    break;
            }
            n *= nScale;
            additionTimes[i] = totalTime / numTrials;
            if (totalTime > MAX_TIME)
                break;
        }
        n = nStart;
        for (int i = 0; i < nSteps; i++) {
            long totalTime = 0L;
            int numTrials = 0;

            if (verbose)
                System.out.println("Multiplication[" + i + "/" + nSteps + ", n=" + n + "]");

            for (; numTrials < MAX_TRIALS; numTrials++) {
                BigInteger A = getRandom(n);
                BigInteger B = getRandom(n);

                startTime = System.nanoTime();
                A.multiply(B);
                totalTime += System.nanoTime() - startTime;

                if (totalTime > MAX_TIME)
                    break;
            }
            n *= nScale;
            multiplicationTimes[i] = totalTime / numTrials;
            if (totalTime > MAX_TIME)
                break;
        }

        //fib testing
        x = xStart;
        xInc = xIncStart;
        for (int i = 0; i < xNSteps; i++) {
            long totalTime = 0L;
            for (int k = 0; k < xScale-1; k++) {

                if (verbose)
                    System.out.println("fibLoop[x=" + x + ", N=" + i + "]");

                int numTrials = 0;
                for (; numTrials < MAX_TRIALS; numTrials++) {

                    startTime = System.nanoTime();
                    FibBigInteger.fibLoop(x);
                    totalTime += System.nanoTime() - startTime;

                    if (totalTime > MAX_TIME)
                        break;
                }
                fibloopTimes[k][i] = totalTime / numTrials;
                if (totalTime > MAX_TIME)
                    break;
                x += xInc;
            }
            if (totalTime > MAX_TIME)
                break;
            xInc *= xScale;
        }

        //mult testing
        x = xStart;
        xInc = xIncStart;
        for (int i = 0; i < xNSteps; i++) {
            long totalTime = 0L;
            for (int k = 0; k < xScale-1; k++) {

                if (verbose)
                    System.out.println("fibMatrix[x=" + x + ", N=" + i + "]");

                int numTrials = 0;
                for (; numTrials < MAX_TRIALS; numTrials++) {

                    startTime = System.nanoTime();
                    FibBigInteger.fibMatrix(x);
                    totalTime += System.nanoTime() - startTime;

                    if (totalTime > MAX_TIME)
                        break;
                }
                fibmatrixTimes[k][i] = totalTime / numTrials;
                if (totalTime > MAX_TIME)
                    break;
                x += xInc;
            }
            if (totalTime > MAX_TIME)
                break;
            xInc *= xScale;
        }

        //need to print
        //additionTimes, multiplicationTimes, fibloopTimes, fibmatrixTimes

        //addition table
        System.out.println("Addition:");
        System.out.format("%8s%12s%10s%10s\n","N", "Time", "2x", "e2x");
        n = nStart;
        System.out.format("%8d%12d%10s%10s\n", n, additionTimes[0], "----", "----");
        n *= nScale;
        for (int i = 1; i < nSteps; i++) {
            System.out.format("%8d%12d%10.3f%10.3f\n", n, additionTimes[i], (double) additionTimes[i] / additionTimes[i-1], 2.0);
            n *= nScale;
        }

        //multiplication table
        System.out.println();
        System.out.println("Multiplication:");
        System.out.format("%8s%12s%10s%10s\n","N", "Time", "2x", "e2x");
        n = nStart;
        System.out.format("%8d%12d%10s%10s\n", n, multiplicationTimes[0], "----", "----");
        n *= nScale;
        for (int i = 1; i < nSteps; i++) {
            System.out.format("%8d%12d%10.3f%10.3f\n", n, multiplicationTimes[i], (double) multiplicationTimes[i] / multiplicationTimes[i-1], Math.pow(n,2)/Math.pow((double)n/2,2));
            n *= nScale;
        }

        //fibloop
        System.out.println();
        System.out.println("Fib Loop:");
        System.out.format("%8s%12s%12s%10s%10s%10s\n", "N", "X", "Time", "10x", "e10x", "e+1");

        x = xStart;
        xInc = xIncStart;
        for (int k = 0; k < xScale-1; k++) { //first loop needed for null spaces
            System.out.format("%8d%12d%12d%10s%10s%10s\n", 1, x, fibloopTimes[k][0], "---", "---", "---");
            x += xInc;
        }
        xInc *= xScale;

        for (int i = 1; i < xNSteps; i++) {
            for (int k = 0; k < xScale-1; k++) {
                System.out.format("%8d%12d%12d%10.3f%10.3f%10.3f\n", i+1, x, fibloopTimes[k][i], (double) fibloopTimes[k][i] / fibloopTimes[k][i-1],
                        Math.pow(x,2)/Math.pow((double)x/2, 2), 0.0);
                x += xInc;
            }
            xInc *= xScale;
        }

        //fibmatrix
        System.out.println();
        System.out.println("Fib Matrix:");
        System.out.format("%8s%12s%12s%10s%10s%10s\n", "N", "X", "Time", "10x", "e10x", "e+1");

        x = xStart;
        xInc = xIncStart;
        for (int k = 0; k < xScale-1; k++) { //first loop needed for null spaces
            System.out.format("%8d%12d%12d%10s%10s%10s\n", 1, x, fibmatrixTimes[k][0], "---", "---", "---");
            x += xInc;
        }
        xInc *= xScale;

        for (int i = 1; i < xNSteps; i++) {
            for (int k = 0; k < xScale-1; k++) {
                System.out.format("%8d%12d%12d%10.3f%10.3f%10.3f\n", i+1, x, fibmatrixTimes[k][i], (double) fibmatrixTimes[k][i] / fibmatrixTimes[k][i-1], 0.0, 0.0);
                x += xInc;
            }
            xInc *= xScale;
        }

        System.out.println();
    }

    public static BigInteger getRandom(int length) {
        Random r = new Random();
        byte[] b = new byte[length];
        r.nextBytes(b);
        return new BigInteger(b);
    }

}
