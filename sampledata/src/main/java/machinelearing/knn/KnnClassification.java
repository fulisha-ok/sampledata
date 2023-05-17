package machinelearing.knn;

import weka.core.Instance;
import weka.core.Instances;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author： fulisha
 * @date： 2023/5/15 9:43
 * @description：
 */
public class KnnClassification {
    /**
     * Manhattan distance.
     */
    public static final int MANHATTAN = 0;

    /**
     * Euclidean distance(欧几里得距离)
     */
    public static final int EUCLIDEAN = 1;

    /**
     * The distance measure
     */
    public int distanceMeasure = EUCLIDEAN;

    /**
     * simple voting
     */
    public static final int VOTE_SIMPLE = 0;

    /**
     * add index to simple voting(用索引做距离的平替)
     */
    public static final int VOTE_DISTANCE_1 = 1;

    /**
     * add distance to simple voting(actual distance)
     */
    public static final int VOTE_DISTANCE_2 = 2;

    /**
     * The vote measure
     */
    public int voteDistance = VOTE_SIMPLE;



    /**
     * a random instance
     */
    public static final Random random = new Random();

    /**
     * The number of neighbors
     */
    int numNeighbors = 7;

    /**
     * The whole dataset
     */
    Instances dataset;

    /**
     *  The training set. Represented by the indices of the data
     */
    int[] trainingSet;

    /**
     * The testing set. Represented by the indices of the data.
     */
    int[] testingSet;

    /**
     * the predictions
     */
    int[] predictions;

    Map<Integer, double[]> distanceMap = new HashMap<>();

    public void setDistanceMeasure(int distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    public void setNumNeighbors(int numNeighbors) {
        this.numNeighbors = numNeighbors;
    }

    public void setVoteDistance(Integer vote) {
        this.voteDistance = vote;
    }
    /**
     * the first constructor
     * @param paraFilename The arff filename.
     */
    public KnnClassification(String paraFilename) {
        try {
            FileReader fileReader  = new FileReader(paraFilename);
            dataset = new Instances(fileReader);
            // the last attribute is the decision class
            dataset.setClassIndex(dataset.numAttributes() - 1);
            fileReader.close();
        } catch (Exception e) {
            System.out.println("Error occurred while trying to read \'" + paraFilename + "\' in KnnClassification constructor.\r\n" + e);
            System.exit(0);
        }
    }

    /**
     * get a random indices for data randomization
     * @param paraLength the length of the sequence
     * @return An array of indices. eg.{4, 3, 1, 5, 0, 2} with length 6
     */
    public static int[] getRandomIndices(int paraLength) {
        int[] resultIndices = new int[paraLength];

        //step1. Initialize
        for (int i = 0; i < paraLength; i++) {
            resultIndices[i] = i;
        }

        //step2 Randomly swap
        int tempFirst, tempSecond, tempValue;
        for (int i = 0; i < paraLength; i++) {
            //Generate two random indices
            tempFirst =  random.nextInt(paraLength);
            tempSecond = random.nextInt(paraLength);

            //swap
            tempValue = resultIndices[tempFirst];
            resultIndices[tempFirst] = resultIndices[tempSecond];
            resultIndices[tempSecond] = tempValue;
        }

        return resultIndices;
    }

    public void splitTrainingTesting(double paraTrainingFraction) {
        int tempSize = dataset.numInstances();
        int[] tempIndices = getRandomIndices(tempSize);
        int tempTrainingSize = (int) (tempSize * paraTrainingFraction);

        trainingSet = new int[tempTrainingSize];
        testingSet = new int[tempSize - tempTrainingSize];

        for (int i = 0; i < tempTrainingSize; i++) {
            trainingSet[i] = tempIndices[i];
        }

        for (int i = 0; i < tempSize - tempTrainingSize; i++) {
            testingSet[i] = tempIndices[tempTrainingSize + i];
        }
    }

    public void splitByIndex(int[] tempIndices, int index) {
        int tempSize = dataset.numInstances();
        int tempTrainingSize  = tempSize - 1;
        testingSet = new int[1];
        trainingSet = new int[tempTrainingSize];
        testingSet[0]  = tempIndices[index];

        int j = 0;
        for (int i = 0; i < tempSize; i++) {
            if (i == index) {
                continue;
            }
            trainingSet[j++] = tempIndices[i];
        }

    }


    public void TrainingTesting() {
        int tempSize = dataset.numInstances();
        int[] tempIndices = getRandomIndices(tempSize);
        int testingSize = 1;

        trainingSet = new int[tempSize - testingSize];
        testingSet = new int[testingSize];

        for (int i = 0; i < tempSize - testingSize; i++) {
            trainingSet[i] = tempIndices[i];
        }

    }


    /**
     * Predict for the whole testing set. The results are stored in predictions
     */
    public void predict() {
        predictions = new int[testingSet.length];
        for (int i = 0; i < predictions.length; i++) {
            predictions[i] = predict(testingSet[i]);
        }
    }

    /**
     * Predict for given instance
     * @param paraIndex
     * @return
     */
    public int predict(int paraIndex) {
        int[] tempNeighbors = computeNearests(paraIndex);
        //int resultPrediction = simpleVoting(tempNeighbors);
        int resultPrediction = weightedVoting(tempNeighbors, voteDistance, distanceMap.get(paraIndex));

        return resultPrediction;
    }

    /**
     * The distance between two instance
     * @param paraI The index of the first instance
     * @param paraJ The index of the second instance
     * @return The distance
     */
    public double distance(int paraI, int paraJ) {
        double resultDistance = 0;
        double tempDifference;
        switch (distanceMeasure) {
            case MANHATTAN:
                for (int i = 0; i < dataset.numAttributes() - 1; i++) {
                    tempDifference = dataset.instance(paraI).value(i) - dataset.instance(paraJ).value(i);
                    if (tempDifference < 0) {
                        resultDistance -= tempDifference;
                    }else {
                        resultDistance += tempDifference;
                    }
                }
                break;

            case EUCLIDEAN:
                for (int i = 0; i < dataset.numAttributes() - 1; i++) {
                    tempDifference = dataset.instance(paraI).value(i) - dataset.instance(paraJ).value(i);
                    resultDistance += tempDifference*tempDifference;
                }
                break;

            default:
                System.out.println("Unsupported distance measure: " + distanceMeasure);
        }

        return resultDistance;
    }

    /**
     * Get the accuracy of the classifier
     * @return
     */
    public double getAccuracy() {
        // A double divides an int gets another double
        double tempCorrect = 0;
        for (int i = 0; i < predictions.length; i++) {
            if (predictions[i] == dataset.instance(testingSet[i]).classValue()) {
                tempCorrect++;
            }
         }

        return tempCorrect / testingSet.length;
    }

    /**
     * compute the nearnest k neighbors.select one neighbor in each scan.
     * @param paraCurrent
     * @return
     */
    public int[] computeNearests(int paraCurrent) {
        int[] resultNearests = new int[numNeighbors];
        double[] resultDistance = new double[numNeighbors];
        boolean[] tempSelected = new boolean[trainingSet.length];
        double tempMinimalDistance;
        int tempMinimalIndex = 0;

        //compute all distance to avoid redundant computation
        double[] tempDistances = new double[trainingSet.length];
        for (int i = 0; i < trainingSet.length; i ++) {
            tempDistances[i] = distance(paraCurrent, trainingSet[i]);
        }

        // Select the nearest paraK indices.
        for (int i = 0; i < numNeighbors; i++) {
            tempMinimalDistance = Double.MAX_VALUE;

            for (int j = 0; j < trainingSet.length; j++) {
                if (tempSelected[j]) {
                    continue;
                }

                if (tempDistances[j] < tempMinimalDistance) {
                    tempMinimalDistance = tempDistances[j];
                    tempMinimalIndex = j;
                }
            }

            resultNearests[i] = trainingSet[tempMinimalIndex];
            resultDistance[i] = tempDistances[tempMinimalIndex];
            tempSelected[tempMinimalIndex] = true;
        }
        distanceMap.put(paraCurrent, resultDistance);
        System.out.println("The nearest of " + paraCurrent + " are: " + Arrays.toString(resultNearests));
        return resultNearests;
    }


    /**
     * Voting using the instances
     * @param paraNeighbors The indices of the neighbors.
     * @return The predicted label.
     */
    public int simpleVoting(int[] paraNeighbors) {
        int[] tempVotes = new int[dataset.numClasses()]; //对k个邻居，看k个邻居种，那种类型的花最多则返回这个类型的花的索引。
        for (int i = 0; i < paraNeighbors.length; i++) {
            tempVotes[(int) dataset.instance(paraNeighbors[i]).classValue()]++;
        }

        int tempMaximalVotingIndex = 0;
        int tempMaximalVoting = 0;
        for (int i = 0; i < dataset.numClasses(); i++) {
            if (tempVotes[i] > tempMaximalVoting) {
                tempMaximalVoting = tempVotes[i];
                tempMaximalVotingIndex = i;
            }
        }

        return tempMaximalVotingIndex;
    }

    public int weightedVoting(int[] paraNeighbors, Integer weightModel, double[] tempDistances) {
        int[] tempVotes = new int[dataset.numClasses()];
        for (int i = 0; i < paraNeighbors.length; i++) {
            //voting 花类型的索引index
            int index = (int)dataset.instance(paraNeighbors[i]).classValue();
            if (weightModel.equals(VOTE_SIMPLE)) {
                tempVotes[index]++;
            } else if (weightModel.equals(VOTE_DISTANCE_1)) {
                // 因为本身paraNeighbors存储的顺序是按从小到大的顺序存储的，我用i做平替
                tempVotes[index] += 1/(i+1);
            } else if (weightModel.equals(VOTE_DISTANCE_2)) {
                // 用具体的距离方式
                tempVotes[index] += 1/tempDistances[i];
            }
        }

        int tempMaximalVotingIndex = 0;
        int tempMaximalVoting = 0;
        for (int i = 0; i < dataset.numClasses(); i++) {
            if (tempVotes[i] > tempMaximalVoting) {
                tempMaximalVoting = tempVotes[i];
                tempMaximalVotingIndex = i;
            }
        }

        return tempMaximalVotingIndex;
    }

    /**
     *  trainingSet = new int[tempTrainingSize];
     *         testingSet = new int[tempSize - tempTrainingSize];
     *
     *         for (int i = 0; i < tempTrainingSize; i++) {
     *             trainingSet[i] = tempIndices[i];
     *         }
     *
     *         for (int i = 0; i < tempSize - tempTrainingSize; i++) {
     *             testingSet[i] = tempIndices[tempTrainingSize + i];
     *         }
     */

    public void leaveNneOutTesting() {
        System.out.println("leave-one-out---------------");
        int tempSize = dataset.numInstances();
        int[] predicts = new int[tempSize];
        int[] tempIndices = getRandomIndices(tempSize);
        for (int i = 0; i < tempSize; i++) {
            splitByIndex(tempIndices, i);
            int[] neighbors = computeNearests(i);
            int resultPrediction = weightedVoting(neighbors, voteDistance, distanceMap.get(i));
            predicts[i] = resultPrediction;
        }

        System.out.println("The nearest of " + predicts + " are: " + Arrays.toString(predicts));
    }

    public static void main(String[] args) {

        KnnClassification tempClassifier = new KnnClassification("D:/fulisha/iris.arff");
        tempClassifier.leaveNneOutTesting();
        tempClassifier.splitTrainingTesting(0.8);
        tempClassifier.predict();
        System.out.println("The accuracy of the classifier is: " + tempClassifier.getAccuracy());

    }

}
