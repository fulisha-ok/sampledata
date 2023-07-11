package machinelearing.ann;

import java.util.Arrays;
import java.util.Random;

/**
 * @author： fulisha
 * @date： 2023/7/11 9:21
 * @description：
 */
public class AnnLayer {
    /**
     * The number of input.
     */
    int numInput;

    /**
     * The number of output.
     */
    int numOutput;

    /**
     * The learning rate.
     */
    double learningRate;

    /**
     * The mobp.
     */
    double mobp;

    /**
     * The weight matrix.
     */
    double[][] weights;

    /**
     * The delta weight matrix.
     */
    double[][] deltaWeights;

    /**
     * Error on nodes.
     */
    double[] errors;

    /**
     * The inputs.
     */
    double[] input;

    /**
     * The outputs.
     */
    double[] output;

    /**
     * The output after activate.
     */
    double[] activatedOutput;

    /**
     * The inputs.
     */
    Activator activator;

    /**
     * The inputs.
     */
    Random random = new Random();

    /**
     * The first constructor.
     * @param paraActivator The activator.
     */
    public AnnLayer(int paraNumInput, int paraNumOutput, char paraActivator,
                    double paraLearningRate, double paraMobp) {
        numInput = paraNumInput;
        numOutput = paraNumOutput;
        learningRate = paraLearningRate;
        mobp = paraMobp;

        weights = new double[numInput + 1][numOutput];
        deltaWeights = new double[numInput + 1][numOutput];
        for (int i = 0; i < numInput + 1; i++) {
            for (int j = 0; j < numOutput; j++) {
                weights[i][j] = random.nextDouble();
            }
        }

        errors = new double[numInput];

        input = new double[numInput];
        output = new double[numOutput];
        activatedOutput = new double[numOutput];

        activator = new Activator(paraActivator);
    }// Of the first constructor

    /**
     * Set parameters for the activator.
     * @param paraAlpha Alpha. Only valid for certain types.
     * @param paraBeta Beta.
     * @param paraAlpha Alpha.
     */
    public void setParameters(double paraAlpha, double paraBeta, double paraGamma) {
        activator.setAlpha(paraAlpha);
        activator.setBeta(paraBeta);
        activator.setGamma(paraGamma);
    }

    /**
     * Forward prediction.
     * @param paraInput  The input data of one instance.
     * @return The data at the output end.
     */
    public double[] forward(double[] paraInput) {
        //System.out.println("Ann layer forward " + Arrays.toString(paraInput));
        // Copy data.
        for (int i = 0; i < numInput; i++) {
            input[i] = paraInput[i];
        }

        // Calculate the weighted sum for each output.
        for (int i = 0; i < numOutput; i++) {
            output[i] = weights[numInput][i];
            for (int j = 0; j < numInput; j++) {
                output[i] += input[j] * weights[j][i];
            }
            activatedOutput[i] = activator.activate(output[i]);
        }

        return activatedOutput;
    }

    /**
     * Back propagation and change the edge weights.
     */
    public double[] backPropagation(double[] paraErrors) {
        //Step 1. Adjust the errors.
        for (int i = 0; i < paraErrors.length; i++) {
            paraErrors[i] = activator.derive(output[i], activatedOutput[i]) * paraErrors[i];
        }

        //Step 2. Compute current errors.
        for (int i = 0; i < numInput; i++) {
            errors[i] = 0;
            for (int j = 0; j < numOutput; j++) {
                errors[i] += paraErrors[j] * weights[i][j];
                deltaWeights[i][j] = mobp * deltaWeights[i][j]
                        + learningRate * paraErrors[j] * input[i];
                weights[i][j] += deltaWeights[i][j];
            }
        }

        for (int j = 0; j < numOutput; j++) {
            deltaWeights[numInput][j] = mobp * deltaWeights[numInput][j] + learningRate * paraErrors[j];
            weights[numInput][j] += deltaWeights[numInput][j];
        }

        return errors;
    }

    /**
     * I am the last layer, set the errors.
     * @param paraTarget For 3-class data, it is [0, 0, 1], [0, 1, 0] or [1, 0, 0].
     */
    public double[] getLastLayerErrors(double[] paraTarget) {
        double[] resultErrors = new double[numOutput];
        for (int i = 0; i < numOutput; i++) {
            resultErrors[i] = (paraTarget[i] - activatedOutput[i]);
        }

        return resultErrors;
    }

    /**
     * Show me.
     */
    @Override
    public String toString() {
        String resultString = "";
        resultString += "Activator: " + activator;
        resultString += "\r\n weights = " + Arrays.deepToString(weights);
        return resultString;
    }

    /**
     * Unit test.
     */
    public static void unitTest() {
        AnnLayer tempLayer = new AnnLayer(2, 3, 's', 0.01, 0.1);
        double[] tempInput = { 1, 4 };

        System.out.println(tempLayer);

        double[] tempOutput = tempLayer.forward(tempInput);
        System.out.println("Forward, the output is: " + Arrays.toString(tempOutput));

        double[] tempError = tempLayer.backPropagation(tempOutput);
        System.out.println("Back propagation, the error is: " + Arrays.toString(tempError));
    }

    /**
     * Test the algorithm.
     */
    public static void main(String[] args) {
        unitTest();
    }
}
