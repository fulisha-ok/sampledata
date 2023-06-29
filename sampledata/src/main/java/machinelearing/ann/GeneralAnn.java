package machinelearing.ann;

import weka.core.Instances;

import java.io.FileReader;
import java.util.Arrays;
import java.util.Random;

/**
 * @author： fulisha
 * @date： 2023/6/26 10:28
 * @description：
 */
public abstract class GeneralAnn {
    /**
     * The whole dataset.
     */
    Instances dataset;

    /**
     * Number of layers. It is counted according to nodes instead of edges.
     */
    int numLayers;

    /**
     * The number of nodes for each layer, e.g., [3, 4, 6, 2] means that there
     * are 3 input nodes (conditional attributes), 2 hidden layers with 4 and 6
     * nodes, respectively, and 2 class values (binary classification).
     */
    int[] layerNumNodes;

    /**
     * Momentum coefficient.
     */
    public double mobp;

    /**
     * Learning rate.
     */
    public double learningRate;

    /**
     * For random number generation.
     */
    Random random = new Random();

    /**
     * The first constructor.
     * @param paraFilename
     *            The arff filename.
     * @param paraLayerNumNodes
     *            The number of nodes for each layer (may be different).
     * @param paraLearningRate
     *            Learning rate.
     * @param paraMobp
     *           Momentum coefficient.
     */
    public GeneralAnn(String paraFilename, int[] paraLayerNumNodes, double paraLearningRate,
                      double paraMobp) {
        // Step 1. Read data.
        try {
            FileReader tempReader = new FileReader(paraFilename);
            dataset = new Instances(tempReader);
            // The last attribute is the decision class.
            dataset.setClassIndex(dataset.numAttributes() - 1);
            tempReader.close();
        } catch (Exception ee) {
            System.out.println("Error occurred while trying to read \'" + paraFilename
                    + "\' in GeneralAnn constructor.\r\n" + ee);
            System.exit(0);
        }

        // Step 2. Accept parameters.
        layerNumNodes = paraLayerNumNodes;
        numLayers = layerNumNodes.length;
        // Adjust if necessary.
        layerNumNodes[0] = dataset.numAttributes() - 1;
        layerNumNodes[numLayers - 1] = dataset.numClasses();
        learningRate = paraLearningRate;
        mobp = paraMobp;
    }

    /**
     * Forward prediction.
     * @param paraInput
     *            The input data of one instance.
     * @return The data at the output end.
     */
    public abstract double[] forward(double[] paraInput);

    /**
     * Back propagation.
     * @param paraTarget
     *            For 3-class data, it is [0, 0, 1], [0, 1, 0] or [1, 0, 0].
     */
    public abstract void backPropagation(double[] paraTarget);

    /**
     * Train using the dataset.
     */
    public void train() {
        double[] tempInput = new double[dataset.numAttributes() - 1];
        double[] tempTarget = new double[dataset.numClasses()];
        for (int i = 0; i < dataset.numInstances(); i++) {
            // Fill the data.
            for (int j = 0; j < tempInput.length; j++) {
                tempInput[j] = dataset.instance(i).value(j);
            }

            // Fill the class label.
            Arrays.fill(tempTarget, 0);
            tempTarget[(int) dataset.instance(i).classValue()] = 1;

            // Train with this instance.
            forward(tempInput);
            backPropagation(tempTarget);
        }
    }

    /**
     * Get the index corresponding to the max value of the array.
     *
     * @return the index.
     */
    public static int argmax(double[] paraArray) {
        int resultIndex = -1;
        double tempMax = -1e10;
        for (int i = 0; i < paraArray.length; i++) {
            if (tempMax < paraArray[i]) {
                tempMax = paraArray[i];
                resultIndex = i;
            }
        }
        return resultIndex;
    }

    /**
     * Test using the dataset.
     *
     * @return The precision.
     */
    public double test() {
        double[] tempInput = new double[dataset.numAttributes() - 1];

        double tempNumCorrect = 0;
        double[] tempPrediction;
        int tempPredictedClass = -1;

        for (int i = 0; i < dataset.numInstances(); i++) {
            // Fill the data.
            for (int j = 0; j < tempInput.length; j++) {
                tempInput[j] = dataset.instance(i).value(j);
            }

            // Train with this instance.
            tempPrediction = forward(tempInput);
            //System.out.println("prediction: " + Arrays.toString(tempPrediction));
            tempPredictedClass = argmax(tempPrediction);
            if (tempPredictedClass == (int) dataset.instance(i).classValue()) {
                tempNumCorrect++;
            }
        }

        System.out.println("Correct: " + tempNumCorrect + " out of " + dataset.numInstances());

        return tempNumCorrect / dataset.numInstances();
    }
}
