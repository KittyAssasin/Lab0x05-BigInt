package com.company;

public class Main {

    public static void main(String[] args) {

        int x;
        int xStart = 1;
        int xMax = 1000;
        long startTime;
        int maxTrials = 30;
        int trialCount;
        long maxTime = 5_000_000_000L;

        String[] methodNames = {"fibRecur", "fibCache", "fibLoop", "fibMatrix"};

        int numMethods = methodNames.length;
        long[][] times = new long[numMethods][xMax];

        //multi-method testing loop
        for (int methodID = 0; methodID < numMethods; methodID++) {
            for (x = xStart; x <= xMax; x++) {
                System.out.print(methodNames[methodID] + " x=" + x);
                times[methodID][x-1] = -1; //set time to negative one to show no results when printing
                for (trialCount = 0; (trialCount < maxTrials); trialCount++) {
                    switch (methodID) {
                        case 0 -> {
                            startTime = System.nanoTime();
                            fibRecur(x-1);
                            times[methodID][x-1] += System.nanoTime() - startTime;
                        }
                        case 1 -> {
                            startTime = System.nanoTime();
                            fibCache(x-1);
                            times[methodID][x-1] += System.nanoTime() - startTime;
                        }
                        case 2 -> {
                            startTime = System.nanoTime();
                            fibLoop(x-1);
                            times[methodID][x-1] += System.nanoTime() - startTime;
                        }
                        case 3 -> {
                            startTime = System.nanoTime();
                            fibMatrix(x-1);
                            times[methodID][x-1] += System.nanoTime() - startTime;
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + methodID);
                    }
                }
                System.out.println(" TrialCount=" + trialCount);
                times[methodID][x-1] /= trialCount;
                if (times[methodID][x-1] > maxTime)
                    break;
            }
        }

        //print results
        String column1HeaderFormat = "%10s|%10s|";
        String tableHeaderFormat = "%13s%12s%15s|";
        String tableNoRatioFormat = "%13d%12s%15s|";
        String tableNoDataFormat = "%13s%12s%15s|";
        String column1Format = "%10d|%10d|";
        String tableEntryFormat = "%13d%12.3f%15.3f|";

        //configuration string
        System.out.println("Results with Start=" + xStart + ", xMax=" + xMax + ", maxTrials=" + maxTrials);

        //header
        System.out.format(column1HeaderFormat, "", "");
        for (int i = 0; i < numMethods; i++)
            System.out.format(tableHeaderFormat, "", methodNames[i], "");
        System.out.println();

        System.out.format(column1HeaderFormat, "X", "N (bits)");
        for (int i = 0; i < numMethods; i++)
            System.out.format(tableHeaderFormat, "Time (ns)", "2x Ratio", "Ex. 2x Ratio");
        System.out.println();

        x = xStart;
        //first entry (has N/A entries, needs special handling)
        System.out.format(column1Format, x, (int) Math.ceil(Math.log(x+1)/Math.log(2)));
        for (int i = 0; i < numMethods; i++)
            System.out.format(tableNoRatioFormat, times[i][0], "N/A", "N/A");
        System.out.println();

        //printing remaining entries
        for (x = xStart + 1; x <= xMax; x++) {
            System.out.format(column1Format, x, (int) Math.ceil(Math.log(x+1)/Math.log(2)));
            for (int k = 0; k < numMethods; k++)
                if (times[k][x-1] <= 0) //no value
                    System.out.format(tableNoDataFormat, "-", "-", "-");
                else
                    if (x % 2 == 0) //even
                        switch (k) {
                            case 0 -> { //recur
                                System.out.format(tableEntryFormat, times[k][x-1], (double) times[k][x-1] / times[k][(x-1)/2], Math.exp(x) / Math.exp(x/2));
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

    //fib recursive
    private static int fibRecur(int nth) {
        if (nth <= 1)
            return nth;
        return fibRecur(nth - 1) + fibRecur(nth - 2);
    }

    //fib cached
    private static int fibCache(int nth) {
        int[] cache = new int[nth + 2];
        cache[0] = 0;
        cache[1] = 1;

        for (int i = 2; i <= nth; i++)
            cache[i] = cache[i-1] + cache[i-2];

        return cache[nth];
    }

    //classic fib
    private static int fibLoop(int nth) {
        int a, b, c;
        a = 0;
        b = 1;

        if (nth == 0)
            return a;

        for (int i = 2; i <= nth; i++) {
            c = a + b;
            a = b;
            b = c;
        }

        return b;
    }

    //fibMatrix
    private static int fibMatrix(int nth) {
        if (nth == 0)
            return 0;

        int[][] init = {{1,1},{1,0}};
        Matrix m = new Matrix(init);

        m = m.power(nth - 1);

        return m.get(0,0);
    }
}
