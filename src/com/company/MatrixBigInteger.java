package com.company;

import java.math.BigInteger;

public class MatrixBigInteger {

    private BigInteger[][] matrix;
    public int M;   //num rows
    public int N;   //num columns

    public MatrixBigInteger(int rows, int columns) {
        matrix = new BigInteger[rows][columns];
        M = rows;
        N = columns;
    }

    public MatrixBigInteger(int[][] array2D) {
        M = array2D.length;
        N = array2D[0].length;
        matrix = new BigInteger[M][N];
        for (int i = 0; i < M; i++)
            for (int k = 0; k < N; k++)
                matrix[i][k] = BigInteger.valueOf(array2D[i][k]);
    }

    public BigInteger get(int row, int column) {
        return matrix[row][column];
    }

    public void set(int row, int column, int value) {
        matrix[row][column] = BigInteger.valueOf(value);
    }

    public void set(int row, int column, BigInteger value) {
        matrix[row][column] = value;
    }

    public void elementAddition(int row, int column, BigInteger value) {
        matrix[row][column] = value.add(matrix[row][column]);
    }

    public void elementSubtraction(int row, int column, BigInteger value) {
        matrix[row][column] = matrix[row][column].subtract(value);
    }

    public MatrixBigInteger multiply(MatrixBigInteger b) {
        MatrixBigInteger a = this;
        MatrixBigInteger c = new MatrixBigInteger(a.M, b.N);
        for (int i = 0; i < c.M; i++)
            for (int k = 0; k < c.N; k++)
                for (int m = 0; m < a.N; m++)
                    c.elementAddition(i, k, a.get(i,m).multiply(b.get(m, k)));
        return c;
    }

    public MatrixBigInteger power(int n) {
        MatrixBigInteger result = this;
        for (int i = 1; i < n; i++)
            result = result.multiply(this);
        return result;
    }

    @Override
    public String toString() {
        String returnString = "Matrix[" + M + "," + N + "]{";
        for (int k = 0; k < M; k++) {
            returnString += "[";
            for (int i = 0; i < N-1; i++)
                returnString += this.get(k, i) + ",";
            returnString += this.get(k,N-1) + "]";
        }
        returnString += "}";
        return returnString;
    }
}
