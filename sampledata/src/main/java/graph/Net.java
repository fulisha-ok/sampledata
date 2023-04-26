package graph;

import matrix.IntMatrix;

import java.util.Arrays;

/**
 * @author： fulisha
 * @date： 2023/4/26 15:46
 * @description：
 */
public class Net {

    /**
     * The maximal distance. Do not use Integer.MAX_VALUE.
     */
    public static final int MAX_DISTANCE = 10000;

    int numNodes;

    IntMatrix weightMatrix;

    /**
     * The first constructor. The number of nodes in the graph.
     * @param paraNumNodes
     */
    public Net(int paraNumNodes) {
        numNodes = paraNumNodes;
        weightMatrix = new IntMatrix(numNodes, numNodes);
        for (int i = 0; i < numNodes; i++) {
            // IntMatrix.
            Arrays.fill(weightMatrix.getData()[i], MAX_DISTANCE);
        }
    }

    /**
     * The second constructor. The data matrix.
     * @param paraMatrix
     */
    public Net(int[][] paraMatrix) {
        weightMatrix = new IntMatrix(paraMatrix);
        numNodes = weightMatrix.getRows();
    }

    @Override
    public String toString() {
        String resultString = "This is the weight matrix of the graph.\r\n" + weightMatrix;
        return resultString;
    }

    public int[] dijikstra(int paraSource) {
        // Step 1. Initialize.
        int[] tempDistanceArray = new int[numNodes];
        for (int i = 0; i < numNodes; i++) {
            tempDistanceArray[i] = weightMatrix.getValue(paraSource, i);
        }

        int[] tempParentArray = new int[numNodes];
        Arrays.fill(tempParentArray, paraSource);
        // -1 for no parent.
        tempParentArray[paraSource] = -1;

        // Visited nodes will not be considered further.
        boolean[] tempVisitedArray = new boolean[numNodes];
        tempVisitedArray[paraSource] = true;

        // Step 2. Main loops.
        int tempMinDistance;
        int tempBestNode = -1;
        for (int i = 0; i < numNodes - 1; i++) {
            // Step 2.1 Find out the best next node.
            tempMinDistance = Integer.MAX_VALUE;
            for (int j = 0; j < numNodes; j++) {
                // This node is visited.
                if (tempVisitedArray[j]) {
                    continue;
                }

                if (tempMinDistance > tempDistanceArray[j]) {
                    tempMinDistance = tempDistanceArray[j];
                    tempBestNode = j;
                }
            }

            tempVisitedArray[tempBestNode] = true;

            // Step 2.2 Prepare for the next round.
            for (int j = 0; j < numNodes; j++) {
                // This node is visited.
                if (tempVisitedArray[j]) {
                    continue;
                }

                // This node cannot be reached.
                if (weightMatrix.getValue(tempBestNode, j) >= MAX_DISTANCE) {
                    continue;
                }

                if (tempDistanceArray[j] > tempDistanceArray[tempBestNode]
                        + weightMatrix.getValue(tempBestNode, j)) {
                    // Change the distance.
                    tempDistanceArray[j] = tempDistanceArray[tempBestNode]
                            + weightMatrix.getValue(tempBestNode, j);
                    // Change the parent.
                    tempParentArray[j] = tempBestNode;
                }
            }

            // For test
            System.out.println("The distance to each node: " + Arrays.toString(tempDistanceArray));
            System.out.println("The parent of each node: " + Arrays.toString(tempParentArray));
        }

        // Step 3. Output for debug.
        System.out.println("Finally");
        System.out.println("The distance to each node: " + Arrays.toString(tempDistanceArray));
        System.out.println("The parent of each node: " + Arrays.toString(tempParentArray));
        return tempDistanceArray;
    }

    public int prim() {
        // Step 1. Initialize.
        // Any node can be the source.
        int tempSource = 0;
        int[] tempDistanceArray = new int[numNodes];
        for (int i = 0; i < numNodes; i++) {
            tempDistanceArray[i] = weightMatrix.getValue(tempSource, i);
        }

        int[] tempParentArray = new int[numNodes];
        Arrays.fill(tempParentArray, tempSource);
        // -1 for no parent.
        tempParentArray[tempSource] = -1;

        // Visited nodes will not be considered further.
        boolean[] tempVisitedArray = new boolean[numNodes];
        tempVisitedArray[tempSource] = true;

        // Step 2. Main loops.
        int tempMinDistance;
        int tempBestNode = -1;
        for (int i = 0; i < numNodes - 1; i++) {
            // Step 2.1 Find out the best next node.
            tempMinDistance = Integer.MAX_VALUE;
            for (int j = 0; j < numNodes; j++) {
                // This node is visited.
                if (tempVisitedArray[j]) {
                    continue;
                }

                if (tempMinDistance > tempDistanceArray[j]) {
                    tempMinDistance = tempDistanceArray[j];
                    tempBestNode = j;
                }
            }

            tempVisitedArray[tempBestNode] = true;

            // Step 2.2 Prepare for the next round.
            for (int j = 0; j < numNodes; j++) {
                // This node is visited.
                if (tempVisitedArray[j]) {
                    continue;
                }

                // This node cannot be reached.
                if (weightMatrix.getValue(tempBestNode, j) >= MAX_DISTANCE) {
                    continue;
                }

                // Attention: the difference from the Dijkstra algorithm.
                if (tempDistanceArray[j] > weightMatrix.getValue(tempBestNode, j)) {
                    // Change the distance.
                    tempDistanceArray[j] = weightMatrix.getValue(tempBestNode, j);
                    // Change the parent.
                    tempParentArray[j] = tempBestNode;
                }
            }

            // For test
            System.out.println(
                    "The selected distance for each node: " + Arrays.toString(tempDistanceArray));
            System.out.println("The parent of each node: " + Arrays.toString(tempParentArray));
        }

        int resultCost = 0;
        for (int i = 0; i < numNodes; i++) {
            resultCost += tempDistanceArray[i];
        }

        // Step 3. Output for debug.
        System.out.println("Finally");
        System.out.println("The parent of each node: " + Arrays.toString(tempParentArray));
        System.out.println("The total cost: " + resultCost);

        return resultCost;
    }

    public static void main(String args[]) {
        Net tempNet0 = new Net(3);
        System.out.println(tempNet0);

        int[][] tempMatrix1 = { { 0, 9, 3, 6 }, { 5, 0, 2, 4 }, { 3, 2, 0, 1 }, { 2, 8, 7, 0 } };
        Net tempNet1 = new Net(tempMatrix1);
        System.out.println(tempNet1);

        // Dijkstra
        tempNet1.dijikstra(1);
        System.out.println("================================");
        // An undirected net is required.
        int[][] tempMatrix2 = { { 0, 7, MAX_DISTANCE, 5, MAX_DISTANCE }, { 7, 0, 8, 9, 7 },
                { MAX_DISTANCE, 8, 0, MAX_DISTANCE, 5 }, { 5, 9, MAX_DISTANCE, 0, 15 },
                { MAX_DISTANCE, 7, 5, 15, 0 } };
        Net tempNet2 = new Net(tempMatrix2);
        tempNet2.prim();
    }

}
