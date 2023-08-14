package machinelearing.cnn;

/**
 * @author： fulisha
 * @date： 2023/7/29 16:07
 * @description：
 */
public class CnnLayer {
    /**
     * The type of the layer.
     */
    LayerTypeEnum type;

    /**
     * The number of out map.-输出特征图数量
     */
    int outMapNum;

    /**
     * The map size. - 特征图尺寸
     */
    Size mapSize;

    /**
     * The kernel size.-卷积核尺寸
     */
    Size kernelSize;

    /**
     * The scale size.-缩放尺寸
     */
    Size scaleSize;

    /**
     * The index of the class (label) attribute. -分类数量（当层类型为OUTPUT时使用）
     */
    int classNum = -1;

    /**
     * Kernel. Dimensions: [front map][out map][width][height].-卷积核
     */
    private double[][][][] kernel;

    /**
     * Bias. The length is outMapNum. - 偏置，长度为输出特征图的数量
     */
    private double[] bias;

    /**
     * Out maps. Dimensions:
     * [batchSize][outMapNum][mapSize.width][mapSize.height]. - 输出特征图，维度：[batch大小][输出特征图数量][特征图宽度][特征图高度]
     */
    private double[][][][] outMaps;

    /**
     * Errors. - 误差
     */
    private double[][][][] errors;

    /**
     * For batch processing. - 用于批处理
     */
    private static int recordInBatch = 0;

    /**
     * The first constructor.
     *
     * @param paraNum
     *            When the type is CONVOLUTION, it is the out map number. when
     *            the type is OUTPUT, it is the class number.
     * @param paraSize
     *            When the type is INPUT, it is the map size; when the type is
     *            CONVOLUTION, it is the kernel size; when the type is SAMPLING,
     *            it is the scale size.
     */
    public CnnLayer(LayerTypeEnum paraType, int paraNum, Size paraSize) {
        type = paraType;
        switch (type) {
            case INPUT:
                outMapNum = 1;
                mapSize = paraSize; // No deep copy.
                break;
            case CONVOLUTION:
                outMapNum = paraNum;
                kernelSize = paraSize;
                break;
            case SAMPLING:
                scaleSize = paraSize;
                break;
            case OUTPUT:
                classNum = paraNum;
                mapSize = new Size(1, 1);
                outMapNum = classNum;
                break;
            default:
                System.out.println("Internal error occurred in AbstractLayer.java constructor.");
        }
    }

    /**
     * Initialize the kernel.
     * @param paraFrontMapNum When the type is CONVOLUTION, it is the out map number. when
     */
    public void initKernel(int paraFrontMapNum) {
        kernel = new double[paraFrontMapNum][outMapNum][][];
        for (int i = 0; i < paraFrontMapNum; i++) {
            for (int j = 0; j < outMapNum; j++) {
                kernel[i][j] = MathUtils.randomMatrix(kernelSize.width, kernelSize.height, true);
            }
        }
    }

    /**
     * Initialize the output kernel. The code is revised to invoke
     * initKernel(int).
     */
    public void initOutputKernel(int paraFrontMapNum, Size paraSize) {
        kernelSize = paraSize;
        initKernel(paraFrontMapNum);
    }

    /**
     * Initialize the bias. No parameter. "int frontMapNum" is claimed however
     * not used.
     */
    public void initBias() {
        bias = MathUtils.randomArray(outMapNum);
    }

    /**
     * Initialize the errors.
     * @param paraBatchSize The batch size.
     */
    public void initErrors(int paraBatchSize) {
        errors = new double[paraBatchSize][outMapNum][mapSize.width][mapSize.height];
    }

    /**
     * Initialize out maps.
     * @param paraBatchSize The batch size.
     */
    public void initOutMaps(int paraBatchSize) {
        outMaps = new double[paraBatchSize][outMapNum][mapSize.width][mapSize.height];
    }

    /**
     * Prepare for a new batch.
     */
    public static void prepareForNewBatch() {
        recordInBatch = 0;
    }


    public static void prepareForNewRecord() {
        recordInBatch++;
    }


    public void setMapValue(int paraMapNo, int paraX, int paraY, double paraValue) {
        outMaps[recordInBatch][paraMapNo][paraX][paraY] = paraValue;
    }

    public void setMapValue(int paraMapNo, double[][] paraOutMatrix) {
        outMaps[recordInBatch][paraMapNo] = paraOutMatrix;
    }


    public Size getMapSize() {
        return mapSize;
    }


    public void setMapSize(Size paraMapSize) {
        mapSize = paraMapSize;
    }


    public LayerTypeEnum getType() {
        return type;
    }


    public int getOutMapNum() {
        return outMapNum;
    }


    public void setOutMapNum(int paraOutMapNum) {
        outMapNum = paraOutMapNum;
    }


    public Size getKernelSize() {
        return kernelSize;
    }


    public Size getScaleSize() {
        return scaleSize;
    }


    public double[][] getMap(int paraIndex) {
        return outMaps[recordInBatch][paraIndex];
    }


    public double[][] getKernel(int paraFrontMap, int paraOutMap) {
        return kernel[paraFrontMap][paraOutMap];
    }

    /**
     * Setter. Set one error.
     */
    public void setError(int paraMapNo, int paraMapX, int paraMapY, double paraValue) {
        errors[recordInBatch][paraMapNo][paraMapX][paraMapY] = paraValue;
    }

    /**
     * Setter. Set one error matrix.
     */
    public void setError(int paraMapNo, double[][] paraMatrix) {
        errors[recordInBatch][paraMapNo] = paraMatrix;
    }

    /**
     * Getter. Get one error matrix.
     */
    public double[][] getError(int paraMapNo) {
        return errors[recordInBatch][paraMapNo];
    }

    /**
     * Getter. Get the whole error tensor.
     */
    public double[][][][] getErrors() {
        return errors;
    }

    /**
     * Setter. Set one kernel.
     */
    public void setKernel(int paraLastMapNo, int paraMapNo, double[][] paraKernel) {
        kernel[paraLastMapNo][paraMapNo] = paraKernel;
    }

    /**
     * Getter.
     */
    public double getBias(int paraMapNo) {
        return bias[paraMapNo];
    }

    /**
     * Setter.
     */
    public void setBias(int paraMapNo, double paraValue) {
        bias[paraMapNo] = paraValue;
    }

    /**
     * Getter.
     */
    public double[][][][] getMaps() {
        return outMaps;
    }

    /**
     * Getter.
     */
    public double[][] getError(int paraRecordId, int paraMapNo) {
        return errors[paraRecordId][paraMapNo];
    }

    /**
     * Getter.
     */
    public double[][] getMap(int paraRecordId, int paraMapNo) {
        return outMaps[paraRecordId][paraMapNo];
    }

    /**
     * Getter.
     */
    public int getClassNum() {
        return classNum;
    }

    /**
     * Getter. Get the whole kernel tensor.
     */
    public double[][][][] getKernel() {
        return kernel;
    }
}
