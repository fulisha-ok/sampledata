package graph;

import matrix.IntMatrix;

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

    public static void main(String[] args) {
        System.out.println("Hello!");
        Graph tempGraph = new Graph(3);
        System.out.println(tempGraph);

        // Unit test.
        getConnectivityTest();
    }


}
