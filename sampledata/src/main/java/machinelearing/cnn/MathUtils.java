package machinelearing.cnn;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author： fulisha
 * @date： 2023/7/29 16:02
 * @description：
 */
public class MathUtils {

    /**
     * An interface for different on-demand operators.
     */
    public interface Operator extends Serializable {
        public double process(double value);
    }

    /**
     * The one-minus-the-value operator.
     */
    public static final Operator one_value = new Operator() {
        private static final long serialVersionUID = 3752139491940330714L;

        @Override
        public double process(double value) {
            return 1 - value;
        }
    };

    /**
     * The sigmoid operator.
     */
    public static final Operator sigmoid = new Operator() {
        private static final long serialVersionUID = -1952718905019847589L;

        @Override
        public double process(double value) {
            return 1 / (1 + Math.pow(Math.E, -value));
        }
    };

    /**
     * An interface for operations with two operators.
     */
    interface OperatorOnTwo extends Serializable {
        public double process(double a, double b);
    }

    /**
     * Plus.
     */
    public static final OperatorOnTwo plus = new OperatorOnTwo() {
        private static final long serialVersionUID = -6298144029766839945L;

        @Override
        public double process(double a, double b) {
            return a + b;
        }
    };

    /**
     * Multiply.
     */
    public static OperatorOnTwo multiply = new OperatorOnTwo() {

        private static final long serialVersionUID = -7053767821858820698L;

        @Override
        public double process(double a, double b) {
            return a * b;
        }
    };

    /**
     * Minus.
     */
    public static OperatorOnTwo minus = new OperatorOnTwo() {

        private static final long serialVersionUID = 7346065545555093912L;

        @Override
        public double process(double a, double b) {
            return a - b;
        }
    };

    /**
     * Print a matrix
     */
    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            String line = Arrays.toString(matrix[i]);
            line = line.replaceAll(", ", "\t");
            System.out.println(line);
        }
        System.out.println();
    }

    /**
     * Rotate the matrix 180 degrees.
     */
    public static double[][] rot180(double[][] matrix) {
        matrix = cloneMatrix(matrix);
        int m = matrix.length;
        int n = matrix[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n / 2; j++) {
                double tmp = matrix[i][j];
                matrix[i][j] = matrix[i][n - 1 - j];
                matrix[i][n - 1 - j] = tmp;
            }
        }
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m / 2; i++) {
                double tmp = matrix[i][j];
                matrix[i][j] = matrix[m - 1 - i][j];
                matrix[m - 1 - i][j] = tmp;
            }
        }
        return matrix;
    }// Of rot180

    private static Random myRandom = new Random(2);

    /**
     * Generate a random matrix with the given size. Each value takes value in
     * [-0.005, 0.095].
     */
    public static double[][] randomMatrix(int x, int y, boolean b) {
        double[][] matrix = new double[x][y];
        // int tag = 1;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                matrix[i][j] = (myRandom.nextDouble() - 0.05) / 10;
            }
        }
        return matrix;
    }

    /**
     * Generate a random array with the given length. Each value takes value in
     * [-0.005, 0.095].
     */
    public static double[] randomArray(int len) {
        double[] data = new double[len];
        for (int i = 0; i < len; i++) {
            //data[i] = myRandom.nextDouble() / 10 - 0.05;
            data[i] = 0;
        }
        return data;
    }

    /**
     * Generate a random perm with the batch size.
     */
    public static int[] randomPerm(int size, int batchSize) {
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < batchSize) {
            set.add(myRandom.nextInt(size));
        }
        int[] randPerm = new int[batchSize];
        int i = 0;
        for (Integer value : set) {
            randPerm[i++] = value;
        }
        return randPerm;
    }

    /**
     * Clone a matrix. Do not use it reference directly.
     */
    public static double[][] cloneMatrix(final double[][] matrix) {
        final int m = matrix.length;
        int n = matrix[0].length;
        final double[][] outMatrix = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                outMatrix[i][j] = matrix[i][j];
            }
        }
        return outMatrix;
    }

    /**
     * Matrix operation with the given operator on single operand.
     */
    public static double[][] matrixOp(final double[][] ma, Operator operator) {
        final int m = ma.length;
        int n = ma[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ma[i][j] = operator.process(ma[i][j]);
            } // Of for j
        } // Of for i
        return ma;
    }// Of matrixOp

    /**
     * Matrix operation with the given operator on two operands.
     */
    public static double[][] matrixOp(final double[][] ma, final double[][] mb,
                                      final Operator operatorA, final Operator operatorB, OperatorOnTwo operator) {
        final int m = ma.length;
        int n = ma[0].length;
        if (m != mb.length || n != mb[0].length) {
            throw new RuntimeException("ma.length:" + ma.length + "  mb.length:" + mb.length);
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                double a = ma[i][j];
                if (operatorA != null) {
                    a = operatorA.process(a);
                }
                double b = mb[i][j];
                if (operatorB != null) {
                    b = operatorB.process(b);
                }
                mb[i][j] = operator.process(a, b);
            }
        }
        return mb;
    }

    /**
     * Extend the matrix to a bigger one (a number of times).
     */
    public static double[][] kronecker(final double[][] matrix, final Size scale) {
        final int m = matrix.length;
        int n = matrix[0].length;
        final double[][] outMatrix = new double[m * scale.width][n * scale.height];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int ki = i * scale.width; ki < (i + 1) * scale.width; ki++) {
                    for (int kj = j * scale.height; kj < (j + 1) * scale.height; kj++) {
                        outMatrix[ki][kj] = matrix[i][j];
                    }
                }
            }
        }
        return outMatrix;
    }

    /**
     * Scale the matrix.
     */
    public static double[][] scaleMatrix(final double[][] matrix, final Size scale) {
        int m = matrix.length;
        int n = matrix[0].length;
        final int sm = m / scale.width;
        final int sn = n / scale.height;
        final double[][] outMatrix = new double[sm][sn];
        if (sm * scale.width != m || sn * scale.height != n) {
            throw new RuntimeException("scale matrix");
        }
        final int size = scale.width * scale.height;
        for (int i = 0; i < sm; i++) {
            for (int j = 0; j < sn; j++) {
                double sum = 0.0;
                for (int si = i * scale.width; si < (i + 1) * scale.width; si++) {
                    for (int sj = j * scale.height; sj < (j + 1) * scale.height; sj++) {
                        sum += matrix[si][sj];
                    }
                }
                outMatrix[i][j] = sum / size;
            }
        }
        return outMatrix;
    }

    /**
     * Convolution full to obtain a bigger size. It is used in back-propagation.
     */
    public static double[][] convnFull(double[][] matrix, final double[][] kernel) {
        int m = matrix.length;
        int n = matrix[0].length;
        final int km = kernel.length;
        final int kn = kernel[0].length;
        final double[][] extendMatrix = new double[m + 2 * (km - 1)][n + 2 * (kn - 1)];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                extendMatrix[i + km - 1][j + kn - 1] = matrix[i][j];
            }
        }
        return convnValid(extendMatrix, kernel);
    }

    /**
     * Convolution operation, from a given matrix and a kernel, sliding and sum
     * to obtain the result matrix. It is used in forward.
     */
    public static double[][] convnValid(final double[][] matrix, double[][] kernel) {
        // kernel = rot180(kernel);
        int m = matrix.length;
        int n = matrix[0].length;
        final int km = kernel.length;
        final int kn = kernel[0].length;
        int kns = n - kn + 1;
        final int kms = m - km + 1;
        final double[][] outMatrix = new double[kms][kns];

        for (int i = 0; i < kms; i++) {
            for (int j = 0; j < kns; j++) {
                double sum = 0.0;
                for (int ki = 0; ki < km; ki++) {
                    for (int kj = 0; kj < kn; kj++) {
                        sum += matrix[i + ki][j + kj] * kernel[ki][kj];
                    }
                }
                outMatrix[i][j] = sum;

            }
        }
        return outMatrix;
    }

    /**
     * Convolution on a tensor.
     */
    public static double[][] convnValid(final double[][][][] matrix, int mapNoX,
                                        double[][][][] kernel, int mapNoY) {
        int m = matrix.length;
        int n = matrix[0][mapNoX].length;
        int h = matrix[0][mapNoX][0].length;
        int km = kernel.length;
        int kn = kernel[0][mapNoY].length;
        int kh = kernel[0][mapNoY][0].length;
        int kms = m - km + 1;
        int kns = n - kn + 1;
        int khs = h - kh + 1;
        if (matrix.length != kernel.length) {
            throw new RuntimeException("length");
        }
        final double[][][] outMatrix = new double[kms][kns][khs];
        for (int i = 0; i < kms; i++) {
            for (int j = 0; j < kns; j++) {
                for (int k = 0; k < khs; k++) {
                    double sum = 0.0;
                    for (int ki = 0; ki < km; ki++) {
                        for (int kj = 0; kj < kn; kj++) {
                            for (int kk = 0; kk < kh; kk++) {
                                sum += matrix[i + ki][mapNoX][j + kj][k + kk]
                                        * kernel[ki][mapNoY][kj][kk];
                            }
                        }
                    }
                    outMatrix[i][j][k] = sum;
                }
            }
        }
        return outMatrix[0];
    }

    /**
     * The sigmod operation.
     */
    public static double sigmod(double x) {
        return 1 / (1 + Math.pow(Math.E, -x));
    }

    /**
     * Sum all values of a matrix.
     */
    public static double sum(double[][] error) {
        int m = error.length;
        int n = error[0].length;
        double sum = 0.0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                sum += error[i][j];
            }
        }
        return sum;
    }

    /**
     * Ad hoc sum.
     */
    public static double[][] sum(double[][][][] errors, int j) {
        int m = errors[0][j].length;
        int n = errors[0][j][0].length;
        double[][] result = new double[m][n];
        for (int mi = 0; mi < m; mi++) {
            for (int nj = 0; nj < n; nj++) {
                double sum = 0;
                for (int i = 0; i < errors.length; i++)
                    sum += errors[i][j][mi][nj];
                result[mi][nj] = sum;
            }
        }
        return result;
    }

    /**
     * Get the index of the maximal value for the final classification.
     */
    public static int getMaxIndex(double[] out) {
        double max = out[0];
        int index = 0;
        for (int i = 1; i < out.length; i++) {
            if (out[i] > max) {
                max = out[i];
                index = i;
            }
        }
        return index;
    }

    public static void main(String[] args) {
        // Arrange
        /*double[][] inputMatrix = {
                {1.0, 2.0, 3.0, 4.0},
                {5.0, 6.0, 7.0, 8.0},
                {9.0, 10.0, 11.0, 12.0},
                {13.0, 14.0, 15.0, 16.0}
        };
        Size scale = new Size(2, 2);
        double[][] scaledMatrix = scaleMatrix(inputMatrix, scale);
        System.out.println(scaledMatrix);*/

      /*  double[][] matrix = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };

        double[][] kernel = {
                {0.0, 1.0},
                {2.0, 3.0}
        };

        double[][] result = convnFull(matrix, kernel);

        System.out.println(result);*/

        double[][][][] matrix = {
                {
                        {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}},
                        {{2.0, 3.0, 4.0}, {5.0, 6.0, 7.0}, {8.0, 9.0, 10.0}}
                },
                {
                        {{0.1, 0.2, 0.3}, {0.4, 0.5, 0.6}, {0.7, 0.8, 0.9}},
                        {{0.2, 0.3, 0.4}, {0.5, 0.6, 0.7}, {0.8, 0.9, 1.0}}
                }
        };

        double[][][][] kernel = {
                {
                        {{0.0, 1.0}, {2.0, 3.0}},
                        {{1.0, 2.0}, {3.0, 4.0}}
                },
                {
                        {{0.1, 0.2}, {0.3, 0.4}},
                        {{0.2, 0.3}, {0.4, 0.5}}
                }
        };

        double[][]result =  convnValid(matrix, 0, kernel, 1);

    }
}
