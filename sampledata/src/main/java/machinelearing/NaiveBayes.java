package machinelearing;

import weka.core.Instance;
import weka.core.Instances;

import java.io.FileReader;
import java.util.Arrays;
import java.util.Random;

/**
 * @author： fulisha
 * @date： 2023/6/2 9:22
 * @description：
 */
public class NaiveBayes {
    /**
     * An inner class to store parameters.
     */
    private class GaussianParamters {
        double mu;
        double sigma;

        public GaussianParamters(double paraMu, double paraSigma) {
            mu = paraMu;
            sigma = paraSigma;
        }

        @Override
        public String toString() {
            return "(" + mu + ", " + sigma + ")";
        }
    }

    /**
     * The data.
     */
    Instances dataset;

    /**
     * The number of classes. For binary classification it is 2.
     */
    int numClasses;

    /**
     * The number of instances.
     */
    int numInstances;

    /**
     * The number of conditional attributes.
     */
    int numConditions;

    /**
     * The prediction, including queried and predicted labels.
     */
    int[] predicts;

    /**
     * Class distribution.
     */
    double[] classDistribution;

    /**
     * Class distribution with Laplacian smooth.
     */
    double[] classDistributionLaplacian;

    /**
     * To calculate the conditional probabilities for all classes over all
     * attributes on all values.
     */
    double[][][] conditionalCounts;

    /**
     * The conditional probabilities with Laplacian smooth.
     */
    double[][][] conditionalProbabilitiesLaplacian;

    /**
     * The Guassian parameters.
     */
    GaussianParamters[][] gaussianParameters;

    /**
     * Data type.
     */
    int dataType;

    /**
     * Nominal.
     */
    public static final int NOMINAL = 0;

    /**
     * Numerical.
     */
    public static final int NUMERICAL = 1;


    /**
     * The constructor
     * @param paraFileName The given file.
     */
    public NaiveBayes(String paraFileName) {
        dataset = null;
        try {
            FileReader reader = new FileReader(paraFileName);
            dataset = new Instances(reader);
            reader.close();
        }catch (Exception e) {
            System.out.println("Cannot read the file: " + paraFileName + "\r\n" + e);
            System.exit(0);
        }

        dataset.setClassIndex(dataset.numAttributes() - 1);
        numConditions = dataset.numAttributes() - 1;
        numInstances = dataset.numInstances();
        numClasses = dataset.attribute(numConditions).numValues();
    }

    /**
     * The constructor
     * @param paraInstances  The given file.
     */
    public NaiveBayes(Instances paraInstances) {
        dataset = paraInstances;

        dataset.setClassIndex(dataset.numAttributes() - 1);
        numConditions = dataset.numAttributes() - 1;
        numInstances = dataset.numInstances();
        numClasses = dataset.attribute(numConditions).numValues();
    }

    /**
     * Set the data type.
     * @param paraDataType
     */
    public void setDataType(int paraDataType) {
        dataType = paraDataType;
    }

    /**
     * Calculate the class distribution with Laplacian smooth.
     */
    public void calculateClassDistribution() {
        classDistribution = new double[numClasses];
        classDistributionLaplacian = new double[numClasses];

        double[] tempCounts = new double[numClasses];
        for (int i = 0; i < numInstances; i++) {
            int tempClassValue = (int) dataset.instance(i).classValue();
            tempCounts[tempClassValue]++;
        }

        for (int i = 0; i < numClasses; i++) {
            classDistribution[i] = tempCounts[i] / numInstances;
            classDistributionLaplacian[i] = (tempCounts[i] + 1) / (numInstances + numClasses);
        }

        System.out.println("Class distribution: " + Arrays.toString(classDistribution));
        System.out.println("Class distribution Laplacian: " + Arrays.toString(classDistributionLaplacian));
    }


    /**
     *  Calculate the conditional probabilities with Laplacian smooth. ONLY scan the dataset once.
     *  There was a simpler one, I have removed it because the time complexity is higher.
     */
    public void calculateConditionalProbabilities() {
        conditionalCounts = new double[numClasses][numConditions][];
        conditionalProbabilitiesLaplacian = new double[numClasses][numConditions][];

        // Allocate space
        for (int i = 0; i < numClasses; i++) {
            for (int j = 0; j < numConditions; j++) {
                int tempNumValues = (int) dataset.attribute(j).numValues();
                conditionalCounts[i][j] = new double[tempNumValues];
                conditionalProbabilitiesLaplacian[i][j] = new double[tempNumValues];
            }
        }

        // Count the numbers
        int[] tempClassCounts = new int[numClasses];
        for (int i = 0; i < numInstances; i++) {
            int tempClass = (int) dataset.instance(i).classValue();
            tempClassCounts[tempClass]++;
            for (int j = 0; j < numConditions; j++) {
                int tempValue = (int) dataset.instance(i).value(j);
                conditionalCounts[tempClass][j][tempValue]++;
            }
        }

        // Now for the real probability with Laplacian
        for (int i = 0; i < numClasses; i++) {
            for (int j = 0; j < numConditions; j++) {
                int tempNumValues = (int) dataset.attribute(j).numValues();
                for (int k = 0; k < tempNumValues; k++) {
                    conditionalProbabilitiesLaplacian[i][j][k] = (conditionalCounts[i][j][k] + 1)
                            / (tempClassCounts[i] + tempNumValues);
                    // I wrote a bug here. This is an alternative approach,
                    // however its performance is better in the mushroom dataset.
                    // conditionalProbabilitiesLaplacian[i][j][k] =
                    // (numInstances * conditionalCounts[i][j][k] + 1)
                    // / (numInstances * tempClassCounts[i] + tempNumValues);
                }
            }
        }

        System.out.println("Conditional probabilities: " + Arrays.deepToString(conditionalCounts));
    }

    /**
     *  Calculate the conditional probabilities with Laplacian smooth.
     */
    public void calculateGausssianParameters() {
        gaussianParameters = new GaussianParamters[numClasses][numConditions];

        double[] tempValuesArray = new double[numInstances];
        int tempNumValues = 0;
        double tempSum = 0;

        for (int i = 0; i < numClasses; i++) {
            for (int j = 0; j < numConditions; j++) {
                tempSum = 0;

                // Obtain values for this class.
                tempNumValues = 0;
                for (int k = 0; k < numInstances; k++) {
                    if ((int) dataset.instance(k).classValue() != i) {
                        continue;
                    }

                    tempValuesArray[tempNumValues] = dataset.instance(k).value(j);
                    tempSum += tempValuesArray[tempNumValues];
                    tempNumValues++;
                }

                // Obtain parameters.
                double tempMu = tempSum / tempNumValues;

                double tempSigma = 0;
                for (int k = 0; k < tempNumValues; k++) {
                    tempSigma += (tempValuesArray[k] - tempMu) * (tempValuesArray[k] - tempMu);
                }
                tempSigma /= tempNumValues;
                tempSigma = Math.sqrt(tempSigma);

                gaussianParameters[i][j] = new GaussianParamters(tempMu, tempSigma);
            }
        }

        System.out.println(Arrays.deepToString(gaussianParameters));
    }

    /**
     *  Classify all instances, the results are stored in predicts[].
     */
    public void classify() {
        predicts = new int[numInstances];
        for (int i = 0; i < numInstances; i++) {
            predicts[i] = classify(dataset.instance(i));
        }
    }

    public int classify(Instance paraInstance) {
        if (dataType == NOMINAL) {
            return classifyNominal(paraInstance);
        } else if (dataType == NUMERICAL) {
            return classifyNumerical(paraInstance);
        } // Of if

        return -1;
    }

    /**
     * Classify an instances with nominal data.
     * @param paraInstance
     * @return
     */
    public int classifyNominal(Instance paraInstance) {
        // Find the biggest one
        double tempBiggest = -10000;
        int resultBestIndex = 0;
        for (int i = 0; i < numClasses; i++) {
            double tempPseudoProbability = Math.log(classDistributionLaplacian[i]);
            for (int j = 0; j < numConditions; j++) {
                int tempAttributeValue = (int) paraInstance.value(j);

                tempPseudoProbability += Math.log(conditionalProbabilitiesLaplacian[i][j][tempAttributeValue]);
            }

            if (tempBiggest < tempPseudoProbability) {
                tempBiggest = tempPseudoProbability;
                resultBestIndex = i;
            }
        }

        return resultBestIndex;
    }

    /**
     * Classify an instances with numerical data.
     * @param paraInstance
     * @return
     */
    public int classifyNumerical(Instance paraInstance) {
        // Find the biggest one
        double tempBiggest = -10000;
        int resultBestIndex = 0;

        for (int i = 0; i < numClasses; i++) {
            double tempPseudoProbability = Math.log(classDistributionLaplacian[i]);
            for (int j = 0; j < numConditions; j++) {
                double tempAttributeValue = paraInstance.value(j);
                double tempSigma = gaussianParameters[i][j].sigma;
                double tempMu = gaussianParameters[i][j].mu;

                tempPseudoProbability += -Math.log(tempSigma)
                        - (tempAttributeValue - tempMu) * (tempAttributeValue - tempMu) / (2 * tempSigma * tempSigma);
            }

            if (tempBiggest < tempPseudoProbability) {
                tempBiggest = tempPseudoProbability;
                resultBestIndex = i;
            }
        }

        return resultBestIndex;
    }

    /**
     * Compute accuracy.
     * @return
     */
    public double computeAccuracy() {
        double tempCorrect = 0;
        for (int i = 0; i < numInstances; i++) {
            if (predicts[i] == (int) dataset.instance(i).classValue()) {
                tempCorrect++;
            }
        }

        double resultAccuracy = tempCorrect / numInstances;
        return resultAccuracy;
    }

    /**
     *  Test nominal data.
     */
    public static void testNominal() {
        System.out.println("Hello, Naive Bayes. I only want to test the nominal data.");
        String tempFilename = "C:/Users/fls/Desktop/mushroom.arff";

        NaiveBayes tempLearner = new NaiveBayes(tempFilename);
        tempLearner.setDataType(NOMINAL);
        tempLearner.calculateClassDistribution();
        tempLearner.calculateConditionalProbabilities();
        tempLearner.classify();

        System.out.println("The accuracy is: " + tempLearner.computeAccuracy());
    }

    /**
     * Test numerical data.
     */
    public static void testNumerical() {
        System.out.println("Hello, Naive Bayes. I only want to test the numerical data with Gaussian assumption.");
        // String tempFilename = "D:/data/iris.arff";
        String tempFilename = "C:/Users/fls/Desktop/iris.arff";

        NaiveBayes tempLearner = new NaiveBayes(tempFilename);
        tempLearner.setDataType(NUMERICAL);
        tempLearner.calculateClassDistribution();
        tempLearner.calculateGausssianParameters();
        tempLearner.classify();

        System.out.println("The accuracy is: " + tempLearner.computeAccuracy());
    }

    /**
     * Test this class.
     * @param args
     */
    public static void main(String[] args) {
        testNominal();
        testNumerical();
        // testNominal(0.8);
    }

    /**
     * Get a random indices for data randomization.
     * @param paraLength  The length of the sequence.
     * @return An array of indices, e.g., {4, 3, 1, 5, 0, 2} with length 6.
     */
    public static int[] getRandomIndices(int paraLength) {
        Random random = new Random();
        int[] resultIndices = new int[paraLength];

        // Step 1. Initialize.
        for (int i = 0; i < paraLength; i++) {
            resultIndices[i] = i;
        } // Of for i

        // Step 2. Randomly swap.
        int tempFirst, tempSecond, tempValue;
        for (int i = 0; i < paraLength; i++) {
            // Generate two random indices.
            tempFirst = random.nextInt(paraLength);
            tempSecond = random.nextInt(paraLength);

            // Swap.
            tempValue = resultIndices[tempFirst];
            resultIndices[tempFirst] = resultIndices[tempSecond];
            resultIndices[tempSecond] = tempValue;
        } // Of for i

        return resultIndices;
    }

    /**
     * Split the data into training and testing parts.
     * @param paraDataset
     * @param paraTrainingFraction The fraction of the training set.
     * @return
     */
    public static Instances[] splitTrainingTesting(Instances paraDataset, double paraTrainingFraction) {
        int tempSize = paraDataset.numInstances();
        int[] tempIndices = getRandomIndices(tempSize);
        int tempTrainingSize = (int) (tempSize * paraTrainingFraction);

        // Empty datasets.
        Instances tempTrainingSet = new Instances(paraDataset);
        tempTrainingSet.delete();
        Instances tempTestingSet = new Instances(tempTrainingSet);

        for (int i = 0; i < tempTrainingSize; i++) {
            tempTrainingSet.add(paraDataset.instance(tempIndices[i]));
        } // Of for i

        for (int i = 0; i < tempSize - tempTrainingSize; i++) {
            tempTestingSet.add(paraDataset.instance(tempIndices[tempTrainingSize + i]));
        } // Of for i

        tempTrainingSet.setClassIndex(tempTrainingSet.numAttributes() - 1);
        tempTestingSet.setClassIndex(tempTestingSet.numAttributes() - 1);
        Instances[] resultInstanesArray = new Instances[2];
        resultInstanesArray[0] = tempTrainingSet;
        resultInstanesArray[1] = tempTestingSet;

        return resultInstanesArray;
    }


    /**
     * Classify all instances, the results are stored in predicts[].
     * @param paraTestingSet
     * @return
     */
    public double classify(Instances paraTestingSet) {
        double tempCorrect = 0;
        int[] tempPredicts = new int[paraTestingSet.numInstances()];
        for (int i = 0; i < tempPredicts.length; i++) {
            tempPredicts[i] = classify(paraTestingSet.instance(i));
            if (tempPredicts[i] == (int) paraTestingSet.instance(i).classValue()) {
                tempCorrect++;
            } // Of if
        } // Of for i

        System.out.println("" + tempCorrect + " correct over " + tempPredicts.length + " instances.");
        double resultAccuracy = tempCorrect / tempPredicts.length;
        return resultAccuracy;
    }

    /**
     * Test nominal data.
     * @param paraTrainingFraction
     */
    public static void testNominal(double paraTrainingFraction) {
        System.out.println("Hello, Naive Bayes. I only want to test the nominal data.");
        String tempFilename = "D:/data/mushroom.arff";
        // String tempFilename = "D:/data/voting.arff";

        Instances tempDataset = null;
        try {
            FileReader fileReader = new FileReader(tempFilename);
            tempDataset = new Instances(fileReader);
            fileReader.close();
        } catch (Exception ee) {
            System.out.println("Cannot read the file: " + tempFilename + "\r\n" + ee);
            System.exit(0);
        } // Of try

        Instances[] tempDatasets = splitTrainingTesting(tempDataset, paraTrainingFraction);
        NaiveBayes tempLearner = new NaiveBayes(tempDatasets[0]);
        tempLearner.setDataType(NOMINAL);
        tempLearner.calculateClassDistribution();
        tempLearner.calculateConditionalProbabilities();

        double tempAccuracy = tempLearner.classify(tempDatasets[1]);

        System.out.println("The accuracy is: " + tempAccuracy);
    }
}
