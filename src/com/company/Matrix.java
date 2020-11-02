package com.company;

public class Matrix {

    private int[][] matrix;
    public int M;   //num rows
    public int N;   //num columns

    public Matrix(int rows, int columns) {
        matrix = new int[rows][columns];
        M = rows;
        N = columns;
    }

    public Matrix(int[][] array2D) {
        M = array2D.length;
        N = array2D[0].length;
        matrix = new int[M][N];
        for (int i = 0; i < M; i++)
            for (int k = 0; k < N; k++)
                matrix[i][k] = array2D[i][k];
    }

    public int get(int row, int column) {
        return matrix[row][column];
    }

    public void set(int row, int column, int value) {
        matrix[row][column] = value;
    }

    public void elementAddition(int row, int column, int value) {
        matrix[row][column] += value;
    }

    public void elementSubtraction(int row, int column, int value) {
        matrix[row][column] -= value;
    }

    public Matrix multiply(Matrix b) {
        Matrix a = this;
        Matrix c = new Matrix(a.M, b.N);
        for (int i = 0; i < c.M; i++)
            for (int k = 0; k < c.N; k++)
                for (int m = 0; m < a.N; m++)
                    c.elementAddition(i, k, a.get(i,m) * b.get(m, k));
        return c;
    }

    public Matrix power(int n) {
        Matrix result = this;
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
