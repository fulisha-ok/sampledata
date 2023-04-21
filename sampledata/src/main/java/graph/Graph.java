package graph;

import datastructure.queue.CircleObjectQueue;
import datastructure.stack.ObjectStack;
import matrix.IntMatrix;

import java.time.Year;
import java.util.Arrays;

/**
 * @author： fulisha
 * @date： 2023/4/18 15:43
 * @description：
 */
public class Graph {

    IntMatrix connectivityMatrix;

    /**
     * The first constructor.
     * @param paraNumNodes The number of nodes in the graph.
     */
    public Graph(int paraNumNodes){
        connectivityMatrix = new IntMatrix(paraNumNodes, paraNumNodes);
    }

    /**
     * The second constructor.
     * @param paraMatrix The data matrix.
     */
    public Graph(int[][] paraMatrix){
        connectivityMatrix = new IntMatrix(paraMatrix);
    }

    @Override
    public String toString(){
        return "This is the connectivity matrix of the graph.\r\n" + connectivityMatrix;
    }

    /**
     * Get the connectivity of the graph.
     * @return
     */
    public boolean getConnectivity() throws Exception {
        // Step 1. Initialize accumulated matrix.
        IntMatrix tempConnectivityMatrix = IntMatrix.getIdentityMatrix(connectivityMatrix.getData().length);

        //Step 2. Initialize
        IntMatrix tempMultipliedMatrix = new IntMatrix(connectivityMatrix);

        //Step 3. Determine the actual connectivity.
        for (int i = 0; i < connectivityMatrix.getData().length - 1; i++){
            // M_a = M_a + M^k
            tempConnectivityMatrix.add(tempMultipliedMatrix);
            // M^k
            tempMultipliedMatrix = IntMatrix.multiply(tempMultipliedMatrix, connectivityMatrix);
        }

        // Step 4. Check the connectivity.
        System.out.println("The connectivity matrix is: " + tempConnectivityMatrix);
        int[][] tempData = tempConnectivityMatrix.getData();
        for (int i = 0; i < tempData.length; i++) {
            for (int j = 0; j < tempData.length; j++){
                if (tempData[i][j] == 0){
                    System.out.println("Node " + i + " cannot reach " + j);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Unit test for getConnectivity.
     */
    public static void getConnectivityTest(){
        int[][] tempMatrix = { { 0, 1, 0 }, { 1, 0, 1 }, { 0, 1, 0 } };
        Graph tempGraph2 = new Graph(tempMatrix);
        System.out.println(tempGraph2);

        boolean tempConnected = false;
        try {
            tempConnected = tempGraph2.getConnectivity();
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
        }
        System.out.println("Is the graph connected? " + tempConnected);

        //Test a directed graph. Remove one arc to form a directed graph.
        tempGraph2.connectivityMatrix.setValue(1, 0, 0);
        tempConnected = false;
        try {
            tempConnected = tempGraph2.getConnectivity();
        } catch (Exception ee) {
            System.out.println(ee);
        }

        System.out.println("Is the graph connected? " + tempConnected);

    }

    /**
     * Breadth first Traversal
     * @param paraStartIndex The start index.
     * @return The sequence of the visit.
     */
    boolean[] tempVisitedArray;
    String resultString = "";
    public String breadthFirstTraversal(int paraStartIndex) {
        CircleObjectQueue tempQueue = new CircleObjectQueue();
        int tempNumNodes = connectivityMatrix.getRows();


        // Initialize the queue
        tempVisitedArray[paraStartIndex] = true;
        resultString += paraStartIndex;
        tempQueue.enqueue(paraStartIndex);

        //Now visit the rest of the graph.
        int tempIndex;
        Integer tempInteger = (Integer) tempQueue.dequeue();
        while (tempInteger != null){
            tempIndex = tempInteger.intValue();

            //Enqueue all its unvisited neighbors.
            for (int i = 0; i < tempNumNodes; i++){
                if (tempVisitedArray[i]){
                    // Already visited.
                    continue;
                }
                if (connectivityMatrix.getData()[tempIndex][i] == 0) {
                    //Not directly connected.
                    continue;
                }
                tempVisitedArray[i] = true;
                resultString += i;
                tempQueue.enqueue(i);
            }

            //Take out one from the head.
            tempInteger = (Integer)tempQueue.dequeue();
        }
        return resultString;
    }

    /**
     * Judge connectivity
     * @param
     * @return
     */
    public boolean breadthTraversal(int paraStartIndex) {
        int tempNumNodes = connectivityMatrix.getRows();
        tempVisitedArray = new boolean[tempNumNodes];
        resultString = "";
        breadthFirstTraversal(paraStartIndex);

        for (int i = 0; i < tempNumNodes; i++){
            if (!tempVisitedArray[i]){
                breadthFirstTraversal(i);
                return false;
            }
        }
        return true;
    }


    public String depthFirstTraversal(int  paraStartIndex) {
        ObjectStack tempStack = new ObjectStack();

        int tempNumNodes = connectivityMatrix.getRows();
        tempVisitedArray = new boolean[tempNumNodes];

        tempVisitedArray[paraStartIndex] = true;
        resultString += paraStartIndex;
        tempStack.push(new Integer(paraStartIndex));
        System.out.println("Push " + paraStartIndex);
        System.out.println("Visited " + resultString);

        int tempIndex = paraStartIndex;
        int tempNext;
        Integer tempInteger;
        while (true) {
            tempNext = -1;
            // Find an unvisited neighbor and push
            for (int i = 0; i < tempNumNodes; i++) {
                if (tempVisitedArray[i]) {
                    continue; //Already visited.
                }

                if (connectivityMatrix.getData()[tempIndex][i] == 0) {
                    continue; //Not directly connected.
                }

                tempVisitedArray[i] = true;
                resultString += i;
                tempStack.push(new Integer(i));
                System.out.println("Push " + i);
                tempNext = i;

                break;
            }


            if (tempNext == -1) {
                //there is no neighbor node, pop
                tempInteger = (Integer) tempStack.pop();
                System.out.println("Pop " + tempInteger);
                if (tempStack.isEmpty()) {
                    //No unvisited neighbor。Backtracking to the last one stored in the stack
                    break;
                }else {
                    tempInteger = (Integer) tempStack.pop();
                    tempIndex = tempInteger.intValue();
                    tempStack.push(tempInteger);
                }
            } else {
                tempIndex = tempNext;
            }
        }
        return resultString;

    }

    public boolean depthTraversal(int paraStartIndex){
        int tempNumNodes = connectivityMatrix.getRows();
        tempVisitedArray = new boolean[tempNumNodes];
        resultString = "";
        depthFirstTraversal(paraStartIndex);

        for (int i = 0; i < tempNumNodes; i++){
            if (!tempVisitedArray[i]){
                depthFirstTraversal(i);
                return false;
            }
        }
        return true;
    }
    public static void depthFirstTraversalTest() {
        // Test an undirected graph.
        //int[][] tempMatrix = { { 0, 1, 1, 0 }, { 1, 0, 0, 1 }, { 1, 0, 0, 0}, { 0, 1, 0, 0} };
        // int[][] tempMatrix = { { 0, 1, 1, 0 , 0}, { 1, 0, 0, 1, 0 }, { 1, 0, 0, 1, 0}, { 0, 1, 1, 0, 0}, { 0, 0, 0, 0, 0} };
        int[][] tempMatrix = { { 0, 1, 1, 0 , 0, 0, 0}, { 1, 0, 0, 1, 0, 0, 0 }, { 1, 0, 0, 1, 0, 0, 0}, { 0, 1, 1, 0, 0, 0, 0}, { 0, 0, 0, 0, 0, 1, 1}, { 0, 0, 0, 0, 1, 0, 0}, { 0, 0, 0, 0, 0, 0, 0} };

        Graph tempGraph = new Graph(tempMatrix);
        System.out.println(tempGraph);

        String tempSequence = "";
        try {
            //tempSequence = tempGraph.depthFirstTraversal(0);
            tempGraph.depthTraversal(0);
        } catch (Exception ee) {
            System.out.println(ee);
        } // Of try.

        System.out.println("The depth first order of visit: " +  tempGraph.resultString);
    }

    public static void breadthFirstTraversalTest() {
        // Test an undirected graph.
        //int[][] tempMatrix = { { 0, 1, 1, 0 }, { 1, 0, 0, 1 }, { 1, 0, 0, 1}, { 0, 1, 1, 0} };
        //int[][] tempMatrix = { { 0, 1, 1, 0 , 0}, { 1, 0, 0, 1, 0 }, { 1, 0, 0, 1, 0}, { 0, 1, 1, 0, 0}, { 0, 0, 0, 0, 0} };
        int[][] tempMatrix = { { 0, 1, 1, 0 , 0, 0, 0}, { 1, 0, 0, 1, 0, 0, 0 }, { 1, 0, 0, 1, 0, 0, 0}, { 0, 1, 1, 0, 0, 0, 0}, { 0, 0, 0, 0, 0, 1, 1}, { 0, 0, 0, 0, 1, 0, 0}, { 0, 0, 0, 0, 0, 0, 0} };
        Graph tempGraph = new Graph(tempMatrix);
        System.out.println(tempGraph);

        String tempSequence = "";
        try {
            tempGraph.breadthTraversal(2);
            //tempSequence = tempGraph.breadthFirstTraversal(2);
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
            return;
        }

        System.out.println("The breadth first order of visit: " + tempGraph.resultString);
    }

    /**
     * Coloring. Output all possible schemes.
     * @param paraNumColors  The number of colors.
     */
    public void coloring(int paraNumColors) {
        int tempNumNodes = connectivityMatrix.getRows();
        int[] tempColorScheme = new int[tempNumNodes];
        Arrays.fill(tempColorScheme, -1);


        coloring(paraNumColors, 0, tempColorScheme);
    }

    /**
     *Coloring. Output all possible schemes.
     * @param paraNumColors
     * @param paraCurrentNumNodes
     * @param paraCurrentColoring
     */
    public void coloring(int paraNumColors, int paraCurrentNumNodes, int[] paraCurrentColoring) {
        // Step 1. Initialize.
        int tempNumNodes = connectivityMatrix.getRows();

        System.out.println("coloring: paraNumColors = " + paraNumColors + ", paraCurrentNumNodes = " + paraCurrentNumNodes + ", paraCurrentColoring" + Arrays.toString(paraCurrentColoring));
        // A complete scheme.
        if (paraCurrentNumNodes >= tempNumNodes) {
            System.out.println("Find one:" + Arrays.toString(paraCurrentColoring));
            return;
        }

        // Try all possible colors.
        for (int i = 0; i < paraNumColors; i++) {
            paraCurrentColoring[paraCurrentNumNodes] = i;
            if (!colorConflict(paraCurrentNumNodes + 1, paraCurrentColoring)) {
                coloring(paraNumColors, paraCurrentNumNodes + 1, paraCurrentColoring);
            }
        }
    }

    public boolean colorConflict(int paraCurrentNumNodes, int[] paraColoring) {
        for (int i = 0; i < paraCurrentNumNodes - 1; i++) {
            // No direct connection.
            if (connectivityMatrix.getValue(paraCurrentNumNodes - 1, i) == 0) {
                continue;
            }

            if (paraColoring[paraCurrentNumNodes - 1] == paraColoring[i]) {
                return true;
            }
        }
        return false;
    }

    public static void coloringTest() {
        //int[][] tempMatrix = { { 0, 1, 1, 0 }, { 1, 0, 0, 1 }, { 1, 0, 0, 0 }, { 0, 1, 0, 0 } };
        int[][] tempMatrix = { { 0, 1, 1}, { 1, 0, 0 }, { 1, 0, 0 }};
        Graph tempGraph = new Graph(tempMatrix);
        //tempGraph.coloring(2);
        tempGraph.coloring(3);
    }

    public static void main(String[] args) {
        System.out.println("Hello!");
        Graph tempGraph = new Graph(3);
        System.out.println(tempGraph);

        // Unit test.
        getConnectivityTest();

        breadthFirstTraversalTest();

        depthFirstTraversalTest();

        coloringTest();
    }


}
