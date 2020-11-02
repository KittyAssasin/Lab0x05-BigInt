package com.company;

public class Fib {
    //fib recursive
    public static int fibRecur(int nth) {
        if (nth <= 1)
            return nth;
        return fibRecur(nth - 1) + fibRecur(nth - 2);
    }

    //fib cached
    public static int fibCache(int nth) {
        int[] cache = new int[nth + 2];
        cache[0] = 0;
        cache[1] = 1;

        for (int i = 2; i <= nth; i++)
            cache[i] = cache[i-1] + cache[i-2];

        return cache[nth];
    }

    //classic fib
    public static int fibLoop(int nth) {
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
    public static int fibMatrix(int nth) {
        if (nth == 0)
            return 0;

        int[][] init = {{1,1},{1,0}};
        Matrix m = new Matrix(init);

        m = m.power(nth - 1);

        return m.get(0,0);
    }
}
