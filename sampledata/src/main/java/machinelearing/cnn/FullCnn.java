package machinelearing.cnn;


import java.util.Arrays;
import machinelearing.cnn.Dataset.Instance;
import machinelearing.cnn.MathUtils.Operator;

/**
 * @author： fulisha
 * @date： 2023/8/8 13:48
 * @description：
 */
public class FullCnn {
    /**
     * The value changes.
     */
    private static double ALPHA = 0.85;

    /**
     * A constant.
     */
    public static double LAMBDA = 0;

    /**
     * Manage layers.
     */
    private static LayerBuilder layerBuilder;

    /**
     * Train using a number of instances simultaneously.
     */
    private int batchSize;

    /**
     * Divide the batch size with the given value.
     */
    private Operator divideBatchSize;

    /**
     * Multiply alpha with the given value.
     */
    private Operator multiplyAlpha;

    /**
     * Multiply lambda and alpha with the given value.
     */
    private Operator multiplyLambda;

    /**
     * The first constructor.
     */
    public FullCnn(LayerBuilder paraLayerBuilder, int paraBatchSize) {
        layerBuilder = paraLayerBuilder;
        batchSize = paraBatchSize;
        setup();
        initOperators();
    }

    /**
     * Initialize operators using temporary classes.
     */
    private void initOperators() {
        divideBatchSize = new Operator() {
            private static final long serialVersionUID = 7424011281732651055L;

            @Override
            public double process(double value) {
                return value / batchSize;
            }
        };

        multiplyAlpha = new Operator() {
            private static final long serialVersionUID = 5761368499808006552L;

            @Override
            public double process(double value) {
                return value * ALPHA;
            }
        };

        multiplyLambda = new Operator() {
            private static final long serialVersionUID = 4499087728362870577L;

            @Override
            public double process(double value) {
                return value * (1 - LAMBDA * ALPHA);
            }
        };
    }

    /**
     * Setup according to the layer builder.
     */
    public void setup() {
        CnnLayer tempInputLayer = layerBuilder.getLayer(0);
        tempInputLayer.initOutMaps(batchSize);

        for (int i = 1; i < layerBuilder.getNumLayers(); i++) {
            CnnLayer tempLayer = layerBuilder.getLayer(i);
            CnnLayer tempFrontLayer = layerBuilder.getLayer(i - 1);
            int tempFrontMapNum = tempFrontLayer.getOutMapNum();
            switch (tempLayer.getType()) {
                case INPUT:
                    // Should not be input. Maybe an error should be thrown out.
                    break;
                case CONVOLUTION:
                    tempLayer.setMapSize(
                            tempFrontLayer.getMapSize().subtract(tempLayer.getKernelSize(), 1));
                    tempLayer.initKernel(tempFrontMapNum);
                    tempLayer.initBias();
                    tempLayer.initErrors(batchSize);
                    tempLayer.initOutMaps(batchSize);
                    break;
                case SAMPLING:
                    tempLayer.setOutMapNum(tempFrontMapNum);
                    tempLayer.setMapSize(tempFrontLayer.getMapSize().divide(tempLayer.getScaleSize()));
                    tempLayer.initErrors(batchSize);
                    tempLayer.initOutMaps(batchSize);
                    break;
                case OUTPUT:
                    tempLayer.initOutputKernel(tempFrontMapNum, tempFrontLayer.getMapSize());
                    tempLayer.initBias();
                    tempLayer.initErrors(batchSize);
                    tempLayer.initOutMaps(batchSize);
                    break;
            }
        }
    }

    /**
     * Forward computing.
     */
    private void forward(Instance instance) {
        setInputLayerOutput(instance);
        for (int l = 1; l < layerBuilder.getNumLayers(); l++) {
            CnnLayer tempCurrentLayer = layerBuilder.getLayer(l);
            CnnLayer tempLastLayer = layerBuilder.getLayer(l - 1);
            switch (tempCurrentLayer.getType()) {
                case CONVOLUTION:
                    setConvolutionOutput(tempCurrentLayer, tempLastLayer);
                    break;
                case SAMPLING:
                    setSampOutput(tempCurrentLayer, tempLastLayer);
                    break;
                case OUTPUT:
                    setConvolutionOutput(tempCurrentLayer, tempLastLayer);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Set the in layer output. Given a record, copy its values to the input map.
     * 输入数据实例映射到卷积神经网络的输入层特征图中(直白点就是将读入的数据一一赋值)
     */
    private void setInputLayerOutput(Instance paraRecord) {
        CnnLayer tempInputLayer = layerBuilder.getLayer(0);
        Size tempMapSize = tempInputLayer.getMapSize();
        double[] tempAttributes = paraRecord.getAttributes();
        if (tempAttributes.length != tempMapSize.width * tempMapSize.height) {
            throw new RuntimeException("input record does not match the map size.");
        }

        for (int i = 0; i < tempMapSize.width; i++) {
            for (int j = 0; j < tempMapSize.height; j++) {
                tempInputLayer.setMapValue(0, i, j, tempAttributes[tempMapSize.height * i + j]);
            }
        }
    }

    /**
     * Compute the convolution output according to the output of the last layer.
     * @param paraLastLayer the last layer.
     * @param paraLayer  the current layer.
     */
    private void setConvolutionOutput(final CnnLayer paraLayer, final CnnLayer paraLastLayer) {
        // int mapNum = paraLayer.getOutMapNum(); 输出特征图数量
        final int lastMapNum = paraLastLayer.getOutMapNum();

        // Attention: paraLayer.getOutMapNum() may not be right.
        //对于当前卷积层的每个输出特征图
        for (int j = 0; j < paraLayer.getOutMapNum(); j++) {
            //存储卷积操作的累积结果
            double[][] tempSumMatrix = null;
            //对于上一层的每个特征图
            for (int i = 0; i < lastMapNum; i++) {
                //获取上一层特征图中的第 i 个特征图
                double[][] lastMap = paraLastLayer.getMap(i);
                //获取卷积层中连接到第 i 个上一层特征图的第 j 个卷积核
                double[][] kernel = paraLayer.getKernel(i, j);
                //第一个特征图的卷积操作
                if (tempSumMatrix == null) {
                    // On the first map.
                    tempSumMatrix = MathUtils.convnValid(lastMap, kernel);
                } else {
                    //若不是则进行累加。
                    // Sum up convolution maps
                    tempSumMatrix = MathUtils.matrixOp(MathUtils.convnValid(lastMap, kernel),
                            tempSumMatrix, null, null, MathUtils.plus);
                }
            }

            // Activation.获取当前特征图对应的偏置值
            final double bias = paraLayer.getBias(j);
            //执行激活函数操作.激活函数使用 sigmoid 函数来进行非线性映射，将矩阵中的每个元素加上偏置值并应用 sigmoid 函数
            tempSumMatrix = MathUtils.matrixOp(tempSumMatrix, new Operator() {
                private static final long serialVersionUID = 2469461972825890810L;

                @Override
                public double process(double value) {
                    return MathUtils.sigmod(value + bias);
                }

            });
            //将经过卷积和激活函数处理后的特征图矩阵设置到当前卷积层的第 j 个输出特征图中
            paraLayer.setMapValue(j, tempSumMatrix);
        }
    }

    /**
     * Compute the convolution output according to the output of the last layer.
     * @param paraLastLayer  the last layer.
     * @param paraLayer  the current layer.
     */
    private void setSampOutput(final CnnLayer paraLayer, final CnnLayer paraLastLayer) {
        // int tempLastMapNum = paraLastLayer.getOutMapNum();

        // Attention: paraLayer.outMapNum may not be right.
        for (int i = 0; i < paraLayer.outMapNum; i++) {
            //// 获取上一层的特征图
            double[][] lastMap = paraLastLayer.getMap(i);
            // 获取当前池化层的缩放大小
            Size scaleSize = paraLayer.getScaleSize();
            //对前一层的特征图进行缩放操作
            double[][] sampMatrix = MathUtils.scaleMatrix(lastMap, scaleSize);
            // 将缩放后的特征图设置为当前池化层的输出
            paraLayer.setMapValue(i, sampMatrix);
        }
    }

    /**
     * Train the cnn.
     */
    public void train(Dataset paraDataset, int paraRounds) {
        // 循环迭代 paraRounds 轮
        for (int t = 0; t < paraRounds; t++) {
            System.out.println("Iteration: " + t);
            // 计算每轮迭代需要的 epoch 数量（每个 epoch 包含一个完整的数据集批次训练）
            int tempNumEpochs = paraDataset.size() / batchSize;
            //无法将所有样本划分为完整的批次，因此需要再额外增加一个批次来容纳余下的样本
            if (paraDataset.size() % batchSize != 0) {
                tempNumEpochs++;
            }
            // logger.info("第{}次迭代，epochsNum: {}", t, epochsNum);
            //正确分类数量的变量
            double tempNumCorrect = 0;
            //统计总样本数量的变量
            int tempCount = 0;
            for (int i = 0; i < tempNumEpochs; i++) {
                //当前批次中样本的随机排列索引
                int[] tempRandomPerm = MathUtils.randomPerm(paraDataset.size(), batchSize);
                //准备网络层以处理新的批次
                CnnLayer.prepareForNewBatch();
                // 遍历当前批次的每个样本
                for (int index : tempRandomPerm) {
                    // 训练当前样本，并返回是否预测正确
                    boolean isRight = train(paraDataset.getInstance(index));
                    if (isRight) {
                        tempNumCorrect++;
                    }
                    tempCount++;
                    // 准备网络层以处理新的样本记录
                    CnnLayer.prepareForNewRecord();
                }
                // 更新网络参数
                updateParameters();
                // 打印进度信息
                if (i % 50 == 0) {
                    System.out.print("..");
                    if (i + 50 > tempNumEpochs) {
                        System.out.println();
                    }
                }
            }
            // 计算当前轮迭代的训练精度
            double p = 1.0 * tempNumCorrect / tempCount;
            // 如果满足条件，调整学习率 ALPHA
            if (t % 10 == 1 && p > 0.96) {
                ALPHA = 0.001 + ALPHA * 0.9;
                // logger.info("设置 alpha = {}", ALPHA);
            }
            // 打印当前轮迭代的训练精度
            System.out.println("Training precision: " + p);
            // logger.info("计算精度： {}/{}={}.", right, count, p);
        }
    }

    /**
     * Train the cnn with only one record.
     * @param paraRecord The given record.
     */
    private boolean train(Instance paraRecord) {
        forward(paraRecord);
        boolean result = backPropagation(paraRecord);
        return result;
    }

    /**
     * Back-propagation.
     * @param paraRecord The given record.
     */
    private boolean backPropagation(Instance paraRecord) {
        boolean result = setOutputLayerErrors(paraRecord);
        setHiddenLayerErrors();
        return result;
    }

    /**
     * Update parameters.
     */
    private void updateParameters() {
        for (int l = 1; l < layerBuilder.getNumLayers(); l++) {
            CnnLayer layer = layerBuilder.getLayer(l);
            CnnLayer lastLayer = layerBuilder.getLayer(l - 1);
            switch (layer.getType()) {
                case CONVOLUTION:
                case OUTPUT:
                    updateKernels(layer, lastLayer);
                    updateBias(layer, lastLayer);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     ***********************
     * Update bias.
     ***********************
     */
    private void updateBias(final CnnLayer paraLayer, CnnLayer paraLastLayer) {
        final double[][][][] errors = paraLayer.getErrors();
        // int mapNum = paraLayer.getOutMapNum();

        // Attention: getOutMapNum() may not be correct.
        for (int j = 0; j < paraLayer.getOutMapNum(); j++) {
            double[][] error = MathUtils.sum(errors, j);
            double deltaBias = MathUtils.sum(error) / batchSize;
            double bias = paraLayer.getBias(j) + ALPHA * deltaBias;
            paraLayer.setBias(j, bias);
        }
    }

    /**
     * Update kernels.
     */
    private void updateKernels(final CnnLayer paraLayer, final CnnLayer paraLastLayer) {
        // int mapNum = paraLayer.getOutMapNum();
        int tempLastMapNum = paraLastLayer.getOutMapNum();

        // Attention: getOutMapNum() may not be right
        for (int j = 0; j < paraLayer.getOutMapNum(); j++) {
            for (int i = 0; i < tempLastMapNum; i++) {
                double[][] tempDeltaKernel = null;
                for (int r = 0; r < batchSize; r++) {
                    double[][] error = paraLayer.getError(r, j);
                    if (tempDeltaKernel == null) {
                        tempDeltaKernel = MathUtils.convnValid(paraLastLayer.getMap(r, i), error);
                    } else {
                        tempDeltaKernel = MathUtils.matrixOp(
                                MathUtils.convnValid(paraLastLayer.getMap(r, i), error),
                                tempDeltaKernel, null, null, MathUtils.plus);
                    }
                }

                tempDeltaKernel = MathUtils.matrixOp(tempDeltaKernel, divideBatchSize);
                if (!rangeCheck(tempDeltaKernel, -10, 10)) {
                    System.exit(0);
                }
                double[][] kernel = paraLayer.getKernel(i, j);
                tempDeltaKernel = MathUtils.matrixOp(kernel, tempDeltaKernel, multiplyLambda,
                        multiplyAlpha, MathUtils.plus);
                paraLayer.setKernel(i, j, tempDeltaKernel);
            }
        }
    }

    /**
     * Set errors of all hidden layers.
     */
    private void setHiddenLayerErrors() {
        // System.out.println("setHiddenLayerErrors");
        for (int l = layerBuilder.getNumLayers() - 2; l > 0; l--) {
            CnnLayer layer = layerBuilder.getLayer(l);
            CnnLayer nextLayer = layerBuilder.getLayer(l + 1);
            // System.out.println("layertype = " + layer.getType());
            switch (layer.getType()) {
                case SAMPLING:
                    setSamplingErrors(layer, nextLayer);
                    break;
                case CONVOLUTION:
                    setConvolutionErrors(layer, nextLayer);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Set errors of a sampling layer.
     */
    private void setSamplingErrors(final CnnLayer paraLayer, final CnnLayer paraNextLayer) {
        // int mapNum = layer.getOutMapNum();
        // 获取下一层的特征图数量
        int tempNextMapNum = paraNextLayer.getOutMapNum();
        // Attention: getOutMapNum() may not be correct
        // 循环遍历当前池化层的特征图
         for (int i = 0; i < paraLayer.getOutMapNum(); i++) {
            // 初始化一个临时变量 sum 用于存储误使用卷积操作（通常是 MathUtils.convnFull 函数）将下一层特征图 j 的误差与卷积核进行卷积。这相当于计算了上一层的误差如何影响池化层的输出差的总和 （这相当于在所有影响的区域上进行累积）
            double[][] sum = null;
            // 获取下一层的 j 号特征图的误差和卷积核
            for (int j = 0; j < tempNextMapNum; j++) {
                //下一层的误差矩阵
                double[][] nextError = paraNextLayer.getError(j);
                //下一层的卷积核
                double[][] kernel = paraNextLayer.getKernel(i, j);
                // 计算当前特征图的误差，特征图 j 的误差与卷积核进行卷积。（计算了上一层的误差如何影响池化层的输出）
                if (sum == null) {
                    sum = MathUtils.convnFull(nextError, MathUtils.rot180(kernel));
                } else {
                    // 如果 sum 不为 null，将当前计算的误差与之前的误差累积起来
                    sum = MathUtils.matrixOp(
                            MathUtils.convnFull(nextError, MathUtils.rot180(kernel)), sum, null,
                            null, MathUtils.plus);
                }
            }
            // 将计算得到的误差设置为当前采样层的第 i 个特征图的误差
            paraLayer.setError(i, sum);

            // 检查误差是否超出了给定的范围，如果超出范围，输出警告信息
            if (!rangeCheck(sum, -2, 2)) {
                System.out.println(
                        "setSampErrors, error out of range.\r\n" + Arrays.deepToString(sum));
            }
        }
    }

        /**
         * Set errors of a sampling layer.
         */
    private void setConvolutionErrors(final CnnLayer paraLayer, final CnnLayer paraNextLayer) {
        // System.out.println("setConvErrors");
        for (int m = 0; m < paraLayer.getOutMapNum(); m++) {
            //获取下一层（通常是池化层）的缩放尺寸
            Size tempScale = paraNextLayer.getScaleSize();
            // 获取与当前特征图相关的下一层误差矩阵
            double[][] tempNextLayerErrors = paraNextLayer.getError(m);
            // 获取当前卷积层的特征图
            double[][] tempMap = paraLayer.getMap(m);
            // 对当前特征图执行操作，生成一个新的矩阵(这一步用于准备进一步的误差传播)
            double[][] tempOutMatrix = MathUtils.matrixOp(tempMap, MathUtils.cloneMatrix(tempMap),
                    null, MathUtils.one_value, MathUtils.multiply);
            //在准备好的矩阵和下一层误差之间执行克罗内克积（Kronecker product） 这一步是误差向后传播的一部分
            tempOutMatrix = MathUtils.matrixOp(tempOutMatrix,
                    MathUtils.kronecker(tempNextLayerErrors, tempScale), null, null,
                    MathUtils.multiply);
            // 将计算得到的误差矩阵设置为当前卷积层中第 m 个特征图的误差
            paraLayer.setError(m, tempOutMatrix);

            // System.out.println("range check nextError");
            if (!rangeCheck(tempNextLayerErrors, -10, 10)) {
                System.out.println("setConvErrors, nextError out of range:\r\n"
                        + Arrays.deepToString(tempNextLayerErrors));
                System.out.println("the new errors are:\r\n" + Arrays.deepToString(tempOutMatrix));

                System.exit(0);
            }

            if (!rangeCheck(tempOutMatrix, -10, 10)) {
                System.out.println("setConvErrors, error out of range.");
                System.exit(0);
            }
        }
    }

    /**
     * Set errors of a sampling layer.
     */
    private boolean setOutputLayerErrors(Instance paraRecord) {
        //获取输出层
        CnnLayer tempOutputLayer = layerBuilder.getOutputLayer();
        //获取输出层的特征图数量
        int tempMapNum = tempOutputLayer.getOutMapNum();

        //创建临时数组来存储目标值和输出值
        double[] tempTarget = new double[tempMapNum];
        double[] tempOutMaps = new double[tempMapNum];
        // 从输出层的特征图中获取输出值
        for (int m = 0; m < tempMapNum; m++) {
            double[][] outmap = tempOutputLayer.getMap(m);
            tempOutMaps[m] = outmap[0][0];
        }
        // 获取输入记录的标签（真实值）
        int tempLabel = paraRecord.getLabel().intValue();
        // 将目标值数组中对应标签位置的值设为1，其他位置设为0
        tempTarget[tempLabel] = 1;
        // Log.i(record.getLable() + "outmaps:" +
        // Util.fomart(outmaps)
        // + Arrays.toString(target));
        // 计算输出层的误差并更新误差
        for (int m = 0; m < tempMapNum; m++) {
            // 使用sigmoid函数的导数计算误差
            tempOutputLayer.setError(m, 0, 0,
                    tempOutMaps[m] * (1 - tempOutMaps[m]) * (tempTarget[m] - tempOutMaps[m]));
        }
        // 检查预测是否正确
        return tempLabel == MathUtils.getMaxIndex(tempOutMaps);
    }

    /**
     * Setup the network.
     */
    public void setup(int paraBatchSize) {
        CnnLayer tempInputLayer = layerBuilder.getLayer(0);
        tempInputLayer.initOutMaps(paraBatchSize);

        for (int i = 1; i < layerBuilder.getNumLayers(); i++) {
            CnnLayer tempLayer = layerBuilder.getLayer(i);
            CnnLayer tempLastLayer = layerBuilder.getLayer(i - 1);
            int tempLastMapNum = tempLastLayer.getOutMapNum();
            switch (tempLayer.getType()) {
                case INPUT:
                    break;
                case CONVOLUTION:
                    tempLayer.setMapSize(
                            tempLastLayer.getMapSize().subtract(tempLayer.getKernelSize(), 1));
                    tempLayer.initKernel(tempLastMapNum);
                    tempLayer.initBias();
                    tempLayer.initErrors(paraBatchSize);
                    tempLayer.initOutMaps(paraBatchSize);
                    break;
                case SAMPLING:
                    tempLayer.setOutMapNum(tempLastMapNum);
                    tempLayer.setMapSize(tempLastLayer.getMapSize().divide(tempLayer.getScaleSize()));
                    tempLayer.initErrors(paraBatchSize);
                    tempLayer.initOutMaps(paraBatchSize);
                    break;
                case OUTPUT:
                    tempLayer.initOutputKernel(tempLastMapNum, tempLastLayer.getMapSize());
                    tempLayer.initBias();
                    tempLayer.initErrors(paraBatchSize);
                    tempLayer.initOutMaps(paraBatchSize);
                    break;
            }
        }
    }

    /**
     * Predict for the dataset.
     */
    public int[] predict(Dataset paraDataset) {
        System.out.println("Predicting ... ");
        CnnLayer.prepareForNewBatch();

        int[] resultPredictions = new int[paraDataset.size()];
        double tempCorrect = 0.0;

        Instance tempRecord;
        for (int i = 0; i < paraDataset.size(); i++) {
            tempRecord = paraDataset.getInstance(i);
            forward(tempRecord);
            CnnLayer outputLayer = layerBuilder.getOutputLayer();

            int tempMapNum = outputLayer.getOutMapNum();
            double[] tempOut = new double[tempMapNum];
            for (int m = 0; m < tempMapNum; m++) {
                double[][] outmap = outputLayer.getMap(m);
                tempOut[m] = outmap[0][0];
            }

            resultPredictions[i] = MathUtils.getMaxIndex(tempOut);
            if (resultPredictions[i] == tempRecord.getLabel().intValue()) {
                tempCorrect++;
            }
        }

        System.out.println("Accuracy: " + tempCorrect / paraDataset.size());
        return resultPredictions;
    }

    /**
     * Range check, only for debugging.
     * @param paraMatrix
     * @param paraLowerBound
     * @param paraUpperBound
     */
    public boolean rangeCheck(double[][] paraMatrix, double paraLowerBound, double paraUpperBound) {
        for (int i = 0; i < paraMatrix.length; i++) {
            for (int j = 0; j < paraMatrix[0].length; j++) {
                if ((paraMatrix[i][j] < paraLowerBound) || (paraMatrix[i][j] > paraUpperBound)) {
                    System.out.println("" + paraMatrix[i][j] + " out of range (" + paraLowerBound
                            + ", " + paraUpperBound + ")\r\n");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * The main entrance.
     */
    public static void main(String[] args) {
        LayerBuilder builder = new LayerBuilder();
        // Input layer, the maps are 28*28
        builder.addLayer(new CnnLayer(LayerTypeEnum.INPUT, -1, new Size(28, 28)));
        // Convolution output has size 24*24, 24=28+1-5
        builder.addLayer(new CnnLayer(LayerTypeEnum.CONVOLUTION, 6, new Size(5, 5)));
        // Sampling output has size 12*12,12=24/2
        builder.addLayer(new CnnLayer(LayerTypeEnum.SAMPLING, -1, new Size(2, 2)));
        // Convolution output has size 8*8, 8=12+1-5
        builder.addLayer(new CnnLayer(LayerTypeEnum.CONVOLUTION, 12, new Size(5, 5)));
        // Sampling output has size4×4,4=8/2
        builder.addLayer(new CnnLayer(LayerTypeEnum.SAMPLING, -1, new Size(2, 2)));
        // output layer, digits 0 - 9.
        builder.addLayer(new CnnLayer(LayerTypeEnum.OUTPUT, 10, null));
        // Construct the full CNN.
        FullCnn tempCnn = new FullCnn(builder, 10);

        Dataset tempTrainingSet = new Dataset("D:/sampledata/sampledata/src/data/train.format", ",", 784);

        // Train the model.
        tempCnn.train(tempTrainingSet, 10);
        // tempCnn.predict(tempTrainingSet);
    }
}

