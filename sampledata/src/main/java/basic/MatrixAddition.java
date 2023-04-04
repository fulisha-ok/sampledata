package basic;

import java.util.Arrays;

/**
 * @author： fulisha
 * @date： 2023/4/4 14:04
 * @description： This is the seventh code. Names and comments should follow my style strictly.
 */
public class MatrixAddition {
    public static void main(String[] args) {
        matrixElementSumTest();

        matrixAdditionTest();
    }

    /**
     * Sum the elements of a matrix.
     * @param paraMatrix
     * @return  The sum of all its elements.
     */
    public static int matrixElementSum(int[][] paraMatrix) {
        int resultSum = 0;
        for (int i = 0; i < paraMatrix.length; i++) {
            for (int j = 0; j < paraMatrix[0].length; j++) {
                resultSum += paraMatrix[i][j];
            }
        }

        return resultSum;
    }

    /**
     * Unit test for respective method
     */
    public static void matrixElementSumTest() {
        int[][] tempMatrix = new int[3][4];
        for (int i = 0; i < tempMatrix.length; i++) {
            for (int j = 0; j < tempMatrix[0].length; j++) {
                tempMatrix[i][j] = i * 10 + j;
            }
        }

        System.out.println("The matrix is: \r\n" + Arrays.deepToString(tempMatrix));
        System.out.println("The matrix element sum is: " + matrixElementSum(tempMatrix) + "\r\n");
    }

    /**
     * Add two matrices. Attention: NO error check is provided at this moment.
     * @param paraMatrix1 The first matrix.
     * @param paraMatrix2 The second matrix. It should have the same size as the first one's
     * @return The addition of these matrices.
     */
    public static int[][] matrixAddition(int[][] paraMatrix1, int[][] paraMatrix2) {
        int[][] resultMatrix = new int[paraMatrix1.length][paraMatrix1[0].length];

        for (int i = 0; i < paraMatrix1.length; i++) {
            for (int j = 0; j < paraMatrix1[0].length; j++) {
                resultMatrix[i][j] = paraMatrix1[i][j] + paraMatrix2[i][j];
            }
        }

        return resultMatrix;
    }

    /**
     * Unit test for respective method.
     */
    public static void matrixAdditionTest() {
        int[][] tempMatrix = new int[3][4];
        for (int i = 0; i < tempMatrix.length; i++) {
            for (int j = 0; j < tempMatrix[0].length; j++) {
                tempMatrix[i][j] = i * 10 + j;
            }
        }

        System.out.println("The matrix is: \r\n" + Arrays.deepToString(tempMatrix));
        int[][] tempNewMatrix = matrixAddition(tempMatrix, tempMatrix);
        System.out.println("The new matrix is: \r\n" + Arrays.deepToString(tempNewMatrix));
    }
}
