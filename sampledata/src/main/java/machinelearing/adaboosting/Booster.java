package machinelearing.adaboosting;

import java.io.FileReader;
import java.util.Arrays;

import weka.core.Instance;
import weka.core.Instances;
import common.Common;
import common.SimpleTools;

/**
 * The booster which ensembles base classifiers. <br>
 * Project: Java implementation of the AdaBoosting algorithm.<br>
 */
public class Booster extends Object {

	/**
	 * The training testing scheme: split in two.
	 */
	public static final int SPLIT_IN_TWO = 0;

	/**
	 * The training testing scheme: use training set.
	 */
	public static final int USE_TRAINING_SET = 1;

	/**
	 * The training testing scheme: specify testing set.
	 */
	public static final int SPECIFY_TESTING_SET = 2;

	/**
	 * The training testing scheme.
	 */
	int trainingTestingScheme;

	/**
	 * Base classifier: stump classifier.
	 */
	public static final int STUMP_CLASSIFIER = 0;

	/**
	 * Base classifier: Bayes classifier.
	 */
	public static final int BAYES_CLASSIFIER = 1;

	/**
	 * Base classifier: Gaussian classifier.
	 */
	public static final int Gaussian_CLASSIFIER = 2;

	/**
	 * The training testing scheme.
	 */
	int baseClassifierType;

	/**
	 * Classifiers.
	 */
	SimpleClassifier[] classifiers;

	/**
	 * Number of classifiers.
	 */
	int numClassifiers;

	/**
	 * Whether or not stop after the training error is 0.
	 */
	boolean stopAfterConverge = false;

	/**
	 * The weights of classifiers.
	 */
	double[] classifierWeights;

	/**
	 * The training data.
	 */
	Instances trainingData;

	/**
	 * The testing data.
	 */
	Instances testingData;

	/**
	 * The first constructor. The testing set is the same as the training set.
	 * @param paraTrainingFilename The data filename.
	 */
	public Booster(String paraTrainingFilename) {
		// Step 1. Read training set.
		try {
			FileReader tempFileReader = new FileReader(paraTrainingFilename);
			trainingData = new Instances(tempFileReader);
			tempFileReader.close();
		} catch (Exception ee) {
			System.out.println("Cannot read the file: " + paraTrainingFilename + "\r\n" + ee);
			System.exit(0);
		}

		// Step 2. Set the last attribute as the class index.
		trainingData.setClassIndex(trainingData.numAttributes() - 1);

		// Step 5. The testing data is the same as the training data.
		testingData = trainingData;
		trainingTestingScheme = USE_TRAINING_SET;

		SimpleTools.variableTrackingOutput("****************Data**********\r\n" + trainingData);
	}

	/**
	 *  The second constructor.
	 * @param paraTrainingFilename The training data filename.
	 * @param paraTestingFilename The testing data filename.
	 */
	public Booster(String paraTrainingFilename, String paraTestingFilename) {
		// Step 1. Read the training set.
		this(paraTrainingFilename);

		// Step 2. Read training set.
		try {
			FileReader tempFileReader = new FileReader(paraTestingFilename);
			testingData = new Instances(tempFileReader);
			tempFileReader.close();
		} catch (Exception ee) {
			System.out.println("Cannot read the file: " + paraTestingFilename + "\r\n" + ee);
			System.exit(0);
		}

		// Step 3. Set the last attribute as the class index.
		testingData.setClassIndex(testingData.numAttributes() - 1);

		// Step 4. Change the scheme.
		trainingTestingScheme = SPECIFY_TESTING_SET;
	}


	/**
	 * The third constructor.
	 * @param paraFilename The data filename.
	 * @param paraTrainingFraction The fraction of the training set.
	 */
	public Booster(String paraFilename, double paraTrainingFraction) {
		// Step 1. Read data.
		Instances tempWholeData = null;
		try {
			FileReader tempFileReader = new FileReader(paraFilename);
			tempWholeData = new Instances(tempFileReader);
			tempFileReader.close();
		} catch (Exception ee) {
			System.out.println("Cannot read the file: " + paraFilename + "\r\n" + ee);
			System.exit(0);
		}

		// Step 2. Split in two
		Instances[] tempDataArray = SimpleTools.splitInTwo(tempWholeData, paraTrainingFraction);

		trainingData = tempDataArray[0];
		testingData = tempDataArray[1];

		// Step 3. Set the last attribute as the class index.
		trainingData.setClassIndex(trainingData.numAttributes() - 1);
		testingData.setClassIndex(testingData.numAttributes() - 1);

		// Step 4. The testing data is the same as the training data.
		trainingTestingScheme = SPLIT_IN_TWO;
	}

	/**
	 * Set the number of base classifier, and allocate space for them.
	 * @param paraNumBaseClassifiers The number of base classifier.
	 */
	public void setNumBaseClassifiers(int paraNumBaseClassifiers) {
		numClassifiers = paraNumBaseClassifiers;

		// Step 1. Allocate space (only reference) for classifiers
		classifiers = new SimpleClassifier[numClassifiers];

		// Step 2. Initialize classifier weights.
		classifierWeights = new double[numClassifiers];
	}


	/**
	 * Set the base classifier type.
	 * @param paraType
	 */
	public void setBaseClassifierType(int paraType) {
		baseClassifierType = paraType;
	}


	public void setStopAfterConverge(boolean paraBoolean) {
		stopAfterConverge = paraBoolean;
	}

	/**
	 * Train the booster.
	 */
	public void train() {
		// Step 1. Initialize.
		WeightedInstances tempWeightedInstances = null;
		double tempError;
		numClassifiers = 0;
		SimpleTools.processTrackingOutput("Booster.train() Step 1\r\n");

		// Step 2. Build other classifiers.
		for (int i = 0; i < classifiers.length; i++) {
			Common.runSteps ++;
			// Step 2.1 Construct or adjust the weightedInstances
			if (i == 0) {
				tempWeightedInstances = new WeightedInstances(trainingData);
			} else {
				// Adjust the weights of the data.
				tempWeightedInstances.adjustWeights(classifiers[i - 1].computeCorrectnessArray(),
						classifierWeights[i - 1]);
			}
			SimpleTools.processTrackingOutput("Booster.train() Step 2.1\r\n");

			// Step 2.2 Train the next classifier.
			switch (baseClassifierType) {
			case STUMP_CLASSIFIER:
				classifiers[i] = new StumpClassifier(tempWeightedInstances);
				break;
			case BAYES_CLASSIFIER:
				classifiers[i] = new BayesClassifier(tempWeightedInstances);
				break;
			case Gaussian_CLASSIFIER:
				classifiers[i] = new GaussianClassifier(tempWeightedInstances);
				break;
			default:
				System.out.println(
						"Internal error. Unsupported base classifier type: " + baseClassifierType);
				System.exit(0);
			}
			classifiers[i].train();
			SimpleTools.processTrackingOutput("Booster.train() Step 2.2\r\n");

			// tempAccuracy = classifiers[i].computeTrainingAccuracy();
			//计算加权错误率
			tempError = classifiers[i].computeWeightedError();
			// Set the classifier weight. 弱分类器的权重
			classifierWeights[i] = 0.5 * Math.log(1 / tempError - 1);
			if (classifierWeights[i] < 1e-6) {
				classifierWeights[i] = 0;
			}
				// SimpleTools.variableTrackingOutput("Booster.train()");

			SimpleTools.variableTrackingOutput("Classifier #" + i + " , weighted error = "
					+ tempError + ", weight = " + classifierWeights[i] + "\r\n");

			numClassifiers++;

			// The accuracy is enough. 记录当前训练轮次（迭代）中集成分类器在训练数据上的准确率
			if (stopAfterConverge) {
				double tempTrainingAccuracy = computeTrainingAccuray();
				SimpleTools.variableTrackingOutput(
						"The accuracy of the booster is: " + tempTrainingAccuracy + "\r\n");
				if (tempTrainingAccuracy > 0.999999) {
					SimpleTools.processTrackingOutput(
							"Stop at the round: " + i + " due to converge.\r\n");
					break;
				}
			}
		}
	}

	/**
	 * Classify an instance.
	 * @param paraInstance The given instance.
	 * @return The predicted label.
	 */
	public int classify(Instance paraInstance) {
		double[] tempLabelsCountArray = new double[trainingData.classAttribute().numValues()];
		for (int i = 0; i < numClassifiers; i++) {
			Common.runSteps ++;
			int tempLabel = classifiers[i].classify(paraInstance);
			tempLabelsCountArray[tempLabel] += classifierWeights[i];
		}

		SimpleTools.variableTrackingOutput(Arrays.toString(tempLabelsCountArray));

		int resultLabel = -1;
		double tempMax = -1;
		for (int i = 0; i < tempLabelsCountArray.length; i++) {
			Common.runSteps ++;
			if (tempMax < tempLabelsCountArray[i]) {
				tempMax = tempLabelsCountArray[i];
				resultLabel = i;
			}
		}

		return resultLabel;
	}

	/**
	 * Test the booster on the training data.
	 * @return The classification accuracy.
	 */
	public double test() {
		SimpleTools.processTrackingOutput(
				"Testing on " + testingData.numInstances() + " instances.\r\n");

		return test(testingData);
	}// Of test

	/**
	 * Test with the given data.
	 * @param paraFilename  The testing data filename.
	 * @return The classification accuracy.
	 */
	public double test(String paraFilename) {
		// Step 1. Read data.
		try {
			FileReader tempFileReader = new FileReader(paraFilename);
			testingData = new Instances(tempFileReader);
			tempFileReader.close();
		} catch (Exception ee) {
			System.out.println("Cannot read the file: " + paraFilename + "\r\n" + ee);
			System.exit(0);
		}

		// Step 2. Set the last attribute as the class index.
		testingData.setClassIndex(testingData.numAttributes() - 1);
		return test(trainingData);
	}

	/**
	 * Test the booster.
	 * @param paraInstances  The testing set.
	 * @return The classification accuracy.
	 */
	public double test(Instances paraInstances) {
		double tempCorrect = 0;
		paraInstances.setClassIndex(paraInstances.numAttributes() - 1);

		for (int i = 0; i < paraInstances.numInstances(); i++) {
			Common.runSteps ++;
			Instance tempInstance = paraInstances.instance(i);
			if (classify(tempInstance) == (int) tempInstance.classValue()) {
				tempCorrect++;
			}
		}

		double resultAccuracy = tempCorrect / paraInstances.numInstances();
		SimpleTools.variableTrackingOutput("The accuracy is: " + resultAccuracy);

		return resultAccuracy;
	}

	/**
	 * Compute the training accuracy of the booster. It is not weighted.
	 * @return The training accuracy.
	 */
	public double computeTrainingAccuray() {
		double tempCorrect = 0;

		for (int i = 0; i < trainingData.numInstances(); i++) {
			Common.runSteps ++;
			if (classify(trainingData.instance(i)) == (int) trainingData.instance(i).classValue()) {
				tempCorrect++;
			}
		}

		double tempAccuracy = tempCorrect / trainingData.numInstances();

		return tempAccuracy;
	}

	/**
	 * For integration test.
	 * @param args  Not provided.
	 */
	public static void main(String args[]) {
		System.out.println("Starting AdaBoosting...");
		SimpleTools.processTracking = false;
		SimpleTools.variableTracking = true;
		// Booster tempBooster = new Booster("src/data/wdbc_norm_ex.arff");
		// Booster tempBooster = new Booster("src/data/iris.arff", 0.8);
		Booster tempBooster = new Booster("D:/sampledata/sampledata/src/data/iris.arff");
		//Booster tempBooster = new Booster("C:/Users/fls/Desktop/iris-easy.arff");

		tempBooster.setStopAfterConverge(true);
		tempBooster.setNumBaseClassifiers(20);
		tempBooster.train();

		System.out.println("The training accuracy is: " + tempBooster.computeTrainingAccuray());
		tempBooster.test();
	}

}
