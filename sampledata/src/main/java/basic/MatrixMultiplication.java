package basic;

import java.util.Arrays;

/**
 * @author： fulisha
 * @date： 2023/4/4 14:13
 * @description： This is the eighth code. Names and comments should follow my style strictly.
 */
public class MatrixMultiplication {
    public static void main(String[] args) {
        matrixMultiplicationTest();
    }

    /**
     * Matrix multiplication. The columns of the first matrix should be equal to the rows of the second one.
     * @param paraFirstMatrix The first matrix.
     * @param paraSecondMatrix The second matrix
     * @return The result matrix.
     */
    public static int[][] multiplication(int[][] paraFirstMatrix, int[][] paraSecondMatrix){
        //m*n n*p == m*p
        int m = paraFirstMatrix.length;
        int n = paraFirstMatrix[0].length;
        int p = paraSecondMatrix[0].length;

        // Step 1. Dimension check.
        if (paraSecondMatrix.length != n) {
            System.out.println("The two matrices cannot be multiplied.");
            return null;
        }

        // Step 2. The loop. m*n n*p == m*p
        int[][] resultMatrix = new int[m][p];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < p; j++) {
                for (int k = 0; k < n; k++) {
                    resultMatrix[i][j] += paraFirstMatrix[i][k] * paraSecondMatrix[k][j];
                }
            }
        }

        return resultMatrix;
    }

    public static void  matrixMultiplicationTest(){
        int[][] tempFirstMatrix = new int[2][3];
        for (int i = 0; i < tempFirstMatrix.length; i++) {
            for (int j = 0; j < tempFirstMatrix[0].length; j++) {
                tempFirstMatrix[i][j] = i + j;
            }
        }
        System.out.println("The first matrix is: \r\n" + Arrays.deepToString(tempFirstMatrix));

        int[][] tempSecondMatrix = new int[3][2];
        for (int i = 0; i < tempSecondMatrix.length; i++) {
            for (int j = 0; j < tempSecondMatrix[0].length; j++) {
                tempSecondMatrix[i][j] = i * 10 + j;
            }
        }
        System.out.println("The second matrix is: \r\n" + Arrays.deepToString(tempSecondMatrix));

        int[][] tempThirdMatrix = multiplication(tempFirstMatrix, tempSecondMatrix);
        System.out.println("The third matrix is: \r\n" + Arrays.deepToString(tempThirdMatrix));

        System.out.println("Trying to multiply the first matrix with itself.\r\n");
        tempThirdMatrix = multiplication(tempFirstMatrix, tempFirstMatrix);
        System.out.println("The result matrix is: \r\n" + Arrays.deepToString(tempThirdMatrix));
    }
}
