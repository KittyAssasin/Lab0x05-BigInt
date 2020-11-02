package com.company;

import java.math.BigInteger;
import java.util.Random;

public class Test {

    public static void runTest(int xStart, int xMax, int maxTrials, boolean printInProgress) {
        int x;
        //int xStart = 1;
        //int xMax = 10;
        long startTime;
        //int maxTrials = 30;
        int trialCount;
        long maxTime = 5_000_000_000L;

        String[] methodNames = {"BIAdd", "BIMul", "bigFibLoop", "bigFibMatrix"};

        int numMethods = methodNames.length;
        long[][] times = new long[numMethods][xMax];

        //configuration string
        System.out.println("Testing with Start=" + xStart + ", xMax=" + xMax + ", maxTrials=" + maxTrials + "...");

        //multi-method testing loop
        for (int methodID = 0; methodID < numMethods; methodID++) {
            for (x = xStart; x <= xMax; x++) {
                if (printInProgress)
                    System.out.print(methodNames[methodID] + " x=" + x);
                times[methodID][x-1] = -1; //set time to negative one to show no results when printing
                for (trialCount = 0; (trialCount < maxTrials); trialCount++) {
                    switch (methodID) {
                        case 0 -> {
                            BigInteger bigX = new BigInteger("" + (int) Math.pow(2, x));
                            BigInteger n1 = getRandom(bigX);
                            BigInteger n2 = getRandom(bigX);
                            startTime = System.nanoTime();
                            n1.add(n2);
                            times[methodID][x-1] += System.nanoTime() - startTime;
                        }
                        case 1 -> {
                            BigInteger bigX = new BigInteger("" + (int) Math.pow(2, x));
                            BigInteger n1 = getRandom(bigX);
                            BigInteger n2 = getRandom(bigX);
                            startTime = System.nanoTime();
                            n1.subtract(n2);
                            times[methodID][x-1] += System.nanoTime() - startTime;
                        }
                        case 2 -> {
                            startTime = System.nanoTime();
                            FibBigInteger.fibLoop(x-1);
                            times[methodID][x-1] += System.nanoTime() - startTime;
                        }
                        case 3 -> {
                            startTime = System.nanoTime();
                            FibBigInteger.fibMatrix(x-1);
                            times[methodID][x-1] += System.nanoTime() - startTime;
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + methodID);
                    }
                }
                if (printInProgress)
                    System.out.println(" TrialCount=" + trialCount);
                times[methodID][x-1] /= trialCount;
                if (times[methodID][x-1] > maxTime)
                    break;
            }
        }

        //print results
        String column1HeaderFormat = "%10s|";
        String tableHeaderFormat = "%13s%12s%15s|";
        String tableNoRatioFormat = "%13d%12s%15s|";
        String tableNoDataFormat = "%13s%12s%15s|";
        String column1Format = "%10d|";
        String tableEntryFormat = "%13d%12.3f%15.3f|";

        //header
        System.out.format(column1HeaderFormat, "");
        for (int i = 0; i < numMethods; i++)
            System.out.format(tableHeaderFormat, "", methodNames[i], "");
        System.out.println();

        System.out.format(column1HeaderFormat, "X");
        for (int i = 0; i < numMethods; i++)
            System.out.format(tableHeaderFormat, "Time (ns)", "2x Ratio", "Ex. 2x Ratio");
        System.out.println();

        x = xStart;
        //first entry (has N/A entries, needs special handling)
        System.out.format(column1Format, x);
        for (int i = 0; i < numMethods; i++)
            System.out.format(tableNoRatioFormat, times[i][0], "N/A", "N/A");
        System.out.println();

        //printing remaining entries
        for (x = xStart + 1; x <= xMax; x++) {
            System.out.format(column1Format, x);
            for (int k = 0; k < numMethods; k++)
                if (times[k][x-1] <= 0) //no value
                    System.out.format(tableNoDataFormat, "-", "-", "-");
                else
                if (x % 2 == 0) //even
                    switch (k) {
                        case 0 -> { //recur
                            System.out.format(tableEntryFormat, times[k][x-1], (double) times[k][x-1] / times[k][(x-1)/2], 0.0);
                        }
                        case 1 -> { //cache
                            System.out.format(tableEntryFormat, times[k][x-1], (double) times[k][x-1] / times[k][(x-1)/2], 2.0);
                        }
                        case 2 -> { //loop
                            System.out.format(tableEntryFormat, times[k][x-1], (double) times[k][x-1] / times[k][(x-1)/2], 2.0);
                        }
                        case 3 -> { //matrix
                            System.out.format(tableEntryFormat, times[k][x-1], (double) times[k][x-1] / times[k][(x-1)/2], 2.0);
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + k);
                    }
                else
                    System.out.format(tableNoRatioFormat, times[k][x-1], "-", "-");
            System.out.println();
        }
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
