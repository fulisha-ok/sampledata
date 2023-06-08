package machinelearing.decisiontree;

import weka.core.Instance;
import weka.core.Instances;

import java.io.FileReader;
import java.util.Arrays;

/**
 * @author： fulisha
 * @date： 2023/6/7 16:03
 * @description：
 */
public class ID3 {
    /**
     * The data.
     */
    Instances dataset;

    /**
     * Is this dataset pure (only one label)?
     */
    boolean pure;

    /**
     * The number of classes. For binary classification it is 2.
     */
    int numClasses;

    /**
     * Available instances. Other instances do not belong this branch.
     */
    int[] availableInstances;

    /**
     * Available attributes. Other attributes have been selected in the path
     * from the root.
     */
    int[] availableAttributes;

    /**
     * The selected attribute.
     */
    int splitAttribute;

    /**
     * The children nodes.
     */
    ID3[] children;

    /**
     * My label. Inner nodes also have a label. For example, <outlook = sunny,humidity = high> never appear in the training data, but <humidity = high> is valid in other cases.
     */
    int label;

    /**
     * The prediction, including queried and predicted labels.
     */
    int[] predicts;

    /**
     * Small block cannot be split further.
     */
    static int smallBlockThreshold = 3;

    /**
     * The constructor.
     * @param paraFilename
     */
    public ID3(String paraFilename) {
        dataset = null;
        try {
            FileReader fileReader = new FileReader(paraFilename);
            dataset = new Instances(fileReader);
            fileReader.close();
        }catch (Exception e) {
            System.out.println("Cannot read the file: " + paraFilename + "\r\n" + e);
            System.exit(0);
        }

        dataset.setClassIndex(dataset.numAttributes() - 1);
        numClasses = dataset.classAttribute().numValues();

        availableInstances = new int[dataset.numInstances()];
        for (int i = 0; i < availableInstances.length; i++) {
            availableInstances[i] = i;
        }
        availableAttributes = new int[dataset.numAttributes() - 1];
        for (int i = 0; i < availableAttributes.length; i++) {
            availableAttributes[i] = i;
        }

        // Initialize.
        children = null;
        // Determine the label by simple voting.
        label = getMajorityClass(availableInstances);
        // Determine whether or not it is pure.
        pure = pureJudge(availableInstances);
    }

    public ID3(Instances paraDataset, int[] paraAvailableInstances, int[] paraAvailableAttributes) {
        dataset = paraDataset;
        availableInstances = paraAvailableInstances;
        availableAttributes = paraAvailableAttributes;

        // Initialize.
        children = null;
        // Determine the label by simple voting.
        label = getMajorityClass(availableInstances);
        // Determine whether or not it is pure.
        pure = pureJudge(availableInstances);
    }

    //判断提供的实例值数据纯不纯 就是看类别是否相同 若有不相同 数据就不纯 返回false
    public boolean pureJudge(int[] paraBlock) {
        pure = true;

        for (int i = 1; i < paraBlock.length; i++) {
            if (dataset.instance(paraBlock[i]).classValue() != dataset.instance(paraBlock[0])
                    .classValue()) {
                pure = false;
                break;
            }
        }

        return pure;
    }

    public int getMajorityClass(int[] paraBlock) {
        int[] tempClassCounts = new int[dataset.numClasses()];
        for (int i = 0; i < paraBlock.length; i++) {
            tempClassCounts[(int) dataset.instance(paraBlock[i]).classValue()]++;
        }

        int resultMajorityClass = -1;
        int tempMaxCount = -1;
        for (int i = 0; i < tempClassCounts.length; i++) {
            if (tempMaxCount < tempClassCounts[i]) {
                resultMajorityClass = i;
                tempMaxCount = tempClassCounts[i];
            }
        }

        return resultMajorityClass;
    }

    /**
     * Select the best attribute.
     * @return The best attribute index.
     */
    public int selectBestAttribute() {
        splitAttribute = -1;
        double tempMinimalEntropy = 10000;
        double tempEntropy;
        //循环遍历每个特征值，计算其条件熵
        for (int i = 0; i < availableAttributes.length; i++) {
            tempEntropy = conditionalEntropy(availableAttributes[i]);
            if (tempMinimalEntropy > tempEntropy) {
                tempMinimalEntropy = tempEntropy;
                splitAttribute = availableAttributes[i];
            }
        }
        return splitAttribute;
    }

    /**
     * Compute the conditional entropy of an attribute.
     * @param paraAttribute The given attribute
     * @return The entropy.
     */
    public double conditionalEntropy(int paraAttribute) {
        // Step 1. Statistics.
        int tempNumClasses = dataset.numClasses();
        int tempNumValues = dataset.attribute(paraAttribute).numValues();
        int tempNumInstances = availableInstances.length;
        double[] tempValueCounts = new double[tempNumValues];
        double[][] tempCountMatrix = new double[tempNumValues][tempNumClasses];

        int tempClass, tempValue;
        for (int i = 0; i < tempNumInstances; i++) {
            tempClass = (int) dataset.instance(availableInstances[i]).classValue();
            tempValue = (int) dataset.instance(availableInstances[i]).value(paraAttribute);
            tempValueCounts[tempValue]++;
            tempCountMatrix[tempValue][tempClass]++;
        }

        // Step 2.
        double resultEntropy = 0;
        double tempEntropy, tempFraction;
        for (int i = 0; i < tempNumValues; i++) {
            if (tempValueCounts[i] == 0) {
                continue;
            }
            tempEntropy = 0;
            for (int j = 0; j < tempNumClasses; j++) {
                tempFraction = tempCountMatrix[i][j] / tempValueCounts[i];
                if (tempFraction == 0) {
                    continue;
                }
                double entropy  = -tempFraction * Math.log(tempFraction);
                tempEntropy += entropy;
            }
            double  resultEntropyTest =  tempValueCounts[i] / tempNumInstances * tempEntropy;
            resultEntropy += resultEntropyTest;
        }

        return resultEntropy;
    }

    /**
     * Split the data according to the given attribute
     * @param paraAttribute
     * @return The blocks.
     */
    public int[][] splitData(int paraAttribute) {
        int tempNumValues = dataset.attribute(paraAttribute).numValues();
        // System.out.println("Dataset " + dataset + "\r\n");
        // System.out.println("Attribute " + paraAttribute + " has " +
        // tempNumValues + " values.\r\n");
        int[][] resultBlocks = new int[tempNumValues][];
        int[] tempSizes = new int[tempNumValues];

        // First scan to count the size of each block.
        int tempValue;
        for (int i = 0; i < availableInstances.length; i++) {
            tempValue = (int) dataset.instance(availableInstances[i]).value(paraAttribute);
            tempSizes[tempValue]++;
        }

        // Allocate space.
        for (int i = 0; i < tempNumValues; i++) {
            resultBlocks[i] = new int[tempSizes[i]];
        }

        // Second scan to fill.
        Arrays.fill(tempSizes, 0);
        for (int i = 0; i < availableInstances.length; i++) {
            tempValue = (int) dataset.instance(availableInstances[i]).value(paraAttribute);
            // Copy data.
            resultBlocks[tempValue][tempSizes[tempValue]] = availableInstances[i];
            tempSizes[tempValue]++;
        }

        return resultBlocks;
    }

    public void buildTree() {
        //判断实例索引
        if (pureJudge(availableInstances)) {
            return;
        }
        if (availableInstances.length <= smallBlockThreshold) {
            return;
        }

        //划分孩子
        selectBestAttribute();
        int[][] tempSubBlocks = splitData(splitAttribute);
        children = new ID3[tempSubBlocks.length];

        // Construct the remaining attribute set.
        int[] tempRemainingAttributes = new int[availableAttributes.length - 1];
        for (int i = 0; i < availableAttributes.length; i++) {
            if (availableAttributes[i] < splitAttribute) {
                tempRemainingAttributes[i] = availableAttributes[i];
            } else if (availableAttributes[i] > splitAttribute) {
                tempRemainingAttributes[i - 1] = availableAttributes[i];
            }
        }

        // Construct children.
        for (int i = 0; i < children.length; i++) {
            if ((tempSubBlocks[i] == null) || (tempSubBlocks[i].length == 0)) {
                children[i] = null;
                continue;
            } else {
                // System.out.println("Building children #" + i + " with
                // instances " + Arrays.toString(tempSubBlocks[i]));
                children[i] = new ID3(dataset, tempSubBlocks[i], tempRemainingAttributes);

                // Important code: do this recursively
                children[i].buildTree();
            }
        }
    }

    public int classify(Instance paraInstance) {
        if (children == null) {
            return label;
        }

        ID3 tempChild = children[(int) paraInstance.value(splitAttribute)];
        if (tempChild == null) {
            return label;
        }

        return tempChild.classify(paraInstance);
    }

    public double test(Instances paraDataset) {
        double tempCorrect = 0;
        for (int i = 0; i < paraDataset.numInstances(); i++) {
            if (classify(paraDataset.instance(i)) == (int) paraDataset.instance(i).classValue()) {
                tempCorrect++;
            }
        }

        return tempCorrect / paraDataset.numInstances();
    }

    /**
     *  Test on the training set.
     * @return
     */
    public double selfTest() {
        return test(dataset);
    }

    @Override
    public String toString() {
        String resultString = "";
        String tempAttributeName = dataset.attribute(splitAttribute).name();
        if (children == null) {
            resultString += "class = " + label;
        } else {
            for (int i = 0; i < children.length; i++) {
                if (children[i] == null) {
                    resultString += tempAttributeName + " = "
                            + dataset.attribute(splitAttribute).value(i) + ":" + "class = " + label
                            + "\r\n";
                } else {
                    resultString += tempAttributeName + " = "
                            + dataset.attribute(splitAttribute).value(i) + ":" + children[i]
                            + "\r\n";
                }
            }
        }

        return resultString;
    }

    /**
     *  Test this class.
     */
    public static void id3Test() {
        ID3 tempID3 = new ID3("C:/Users/fls/Desktop/weather.arff");
        // ID3 tempID3 = new ID3("D:/data/mushroom.arff");
        ID3.smallBlockThreshold = 3;
        tempID3.buildTree();

        System.out.println("The tree is: \r\n" + tempID3);

        double tempAccuracy = tempID3.selfTest();
        System.out.println("The accuracy is: " + tempAccuracy);
    }

    public static void main(String[] args) {
        id3Test();
    }

}
