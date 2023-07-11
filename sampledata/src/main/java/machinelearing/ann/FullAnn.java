package machinelearing.ann;

/**
 * @author： fulisha
 * @date： 2023/7/11 9:26
 * @description：
 */
public class FullAnn extends GeneralAnn{

    /**
     * The layers.
     */
    AnnLayer[] layers;

    /**
     * The first constructor.
     * @param paraFilename The arff filename.
     * @param paraLayerNumNodes The number of nodes for each layer (may be different).
     * @param paraLearningRate Learning rate.
     * @param paraMobp Momentum coefficient.
     * @param paraActivators The storing the activators of each layer.
     */
    public FullAnn(String paraFilename, int[] paraLayerNumNodes, double paraLearningRate,
                   double paraMobp, String paraActivators) {
        super(paraFilename, paraLayerNumNodes, paraLearningRate, paraMobp);

        // Initialize layers.
        layers = new AnnLayer[numLayers - 1];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new AnnLayer(layerNumNodes[i], layerNumNodes[i + 1], paraActivators.charAt(i), paraLearningRate,
                    paraMobp);
        }
    }

    /**
     * Forward prediction. This is just a stub and should be overwritten in the subclass.
     * @param paraInput The input data of one instance.
     * @return The data at the output end.

     */
    @Override
    public double[] forward(double[] paraInput) {
        double[] resultArray = paraInput;
        for(int i = 0; i < numLayers - 1; i ++) {
            resultArray = layers[i].forward(resultArray);
        }

        return resultArray;
    }

    /**
     * Back propagation. This is just a stub and should be overwritten in the subclass.
     * @param paraTarget  For 3-class data, it is [0, 0, 1], [0, 1, 0] or [1, 0, 0].
     */
    @Override
    public void backPropagation(double[] paraTarget) {
        double[] tempErrors = layers[numLayers - 2].getLastLayerErrors(paraTarget);
        for (int i = numLayers - 2; i >= 0; i--) {
            tempErrors = layers[i].backPropagation(tempErrors);
        }
        return;
    }

    /**
     * Show me.
     */
    @Override
    public String toString() {
        String resultString = "I am a full ANN with " + numLayers + " layers";
        return resultString;
    }

    /**
     * Test the algorithm.
     */
    public static void main(String[] args) {
        int[] tempLayerNodes = { 4, 8, 8, 3 };
        FullAnn tempNetwork = new FullAnn("D:/sampledata/sampledata/src/data/iris.arff", tempLayerNodes, 0.01,
                0.6, "uuu");

        for (int round = 0; round < 5000; round++) {
            tempNetwork.train();
        }

        double tempAccuray = tempNetwork.test();
        System.out.println("The accuracy is: " + tempAccuray);
        System.out.println("FullAnn ends.");
    }
}
