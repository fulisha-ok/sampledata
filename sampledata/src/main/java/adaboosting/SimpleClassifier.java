package adaboosting;

import common.Common;
import weka.core.Instance;

/**
 * The super class of any simple classifier.<br>
 * Project: Java implementation of the AdaBoosting algorithm.<br>
 */
public abstract class SimpleClassifier extends Object{
	
	/**
	 * The index of the current attribute.
	 */
	int selectedAttribute;

	/**
	 * Weighted data.
	 */
	WeightedInstances weightedInstances;

	/**
	 * The accuracy on the training set.
	 */
	double trainingAccuracy;
	
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
	 * The first constructor.
	 * @param paraWeightedInstances The given instances.
	 */
	public SimpleClassifier(WeightedInstances paraWeightedInstances) {
		weightedInstances = paraWeightedInstances;
		
		numConditions = weightedInstances.numAttributes() - 1;
		numInstances = weightedInstances.numInstances();
		numClasses = weightedInstances.classAttribute().numValues();
	}
	
	/**
	 * Train the classifier.
	 */
	public abstract void train();

	/**
	 * Classify an instance.
	 * @param paraInstance The given instance.
	 * @return Predicted label.
	 */
	public abstract int classify(Instance paraInstance);	
	

	/**
	 *  Which instances in the training set are correctly classified.
	 * @return The correctness array.
	 */
	public boolean[] computeCorrectnessArray() {
		boolean[] resultCorrectnessArray = new boolean[weightedInstances.numInstances()];
		for (int i = 0; i < resultCorrectnessArray.length; i++) {
			Common.runSteps ++;
			Instance tempInstance = weightedInstances.instance(i);
			if ((int) (tempInstance.classValue()) == classify(tempInstance)) {
				resultCorrectnessArray[i] = true;
			}

		}
		return resultCorrectnessArray;
	}


	/**
	 * Compute the accuracy on the training set.
	 * @return The training accuracy
	 */
	public double computeTrainingAccuracy() {
		double tempCorrect = 0;
		boolean[] tempCorrectnessArray = computeCorrectnessArray();
		for (int i = 0; i < tempCorrectnessArray.length; i++) {
			Common.runSteps ++;
			if (tempCorrectnessArray[i]) {
				tempCorrect ++;
			}
		}

		double resultAccuracy = tempCorrect / tempCorrectnessArray.length;

		return resultAccuracy;
	}

	/**
	 * Compute the weighted error on the training set. It is at least 1e-6 to avoid NaN.
	 * @return The weighted error.
	 */
	public double computeWeightedError() {
		double resultError = 0;
		boolean[] tempCorrectnessArray = computeCorrectnessArray();
		for (int i = 0; i < tempCorrectnessArray.length; i++) {
			Common.runSteps ++;
			if (!tempCorrectnessArray[i]) {
				resultError += weightedInstances.getWeight(i);
			}
		}

		if (resultError < 1e-6) {
			resultError = 1e-6;
		}

		return resultError;
	}
}
