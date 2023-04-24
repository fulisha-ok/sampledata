package graph;

import datastructure.queue.CircleObjectQueue;
import datastructure.stack.ObjectStack;

/**
 * @author： fulisha
 * @date： 2023/4/24 11:34
 * @description：
 */
public class AdjacencyList {
    /**
     * An inner class for adjacent node.
     */
    class AdjacencyNode {
        /**
         * The column index.
         */
        int column;
        /**
         * The next adjacent node.
         */
        AdjacencyNode next;

        /**
         * The first constructor.
         *
         * @param paraColumn
         */
        public AdjacencyNode(int paraColumn) {
            column = paraColumn;
            next = null;
        }
    }

    int numNodes;
    AdjacencyNode[] headers;

    public AdjacencyList(int[][] paraMatrix) {
        numNodes = paraMatrix.length;

        // Step 1. Initialize. The data in the headers are not meaningful.
        AdjacencyNode tempPreviousNode, tempNode;

        headers = new AdjacencyNode[numNodes];
        for (int i = 0; i < numNodes; i++) {
            headers[i] = new AdjacencyNode(-1);
            tempPreviousNode = headers[i];
            for (int j = 0; j < numNodes; j++) {
                if (paraMatrix[i][j] == 0) {
                    continue;
                }

                tempNode = new AdjacencyNode(j);
                tempPreviousNode.next = tempNode;
                tempPreviousNode = tempNode;
            }
        }
    }

    @Override
    public String toString() {
        String resultString = "";
        AdjacencyNode tempNode;
        for (int i = 0; i < numNodes; i++) {
            tempNode = headers[i].next;

            while (tempNode != null) {
                resultString += " (" + i + ", " + tempNode.column + ")";
                tempNode = tempNode.next;
            } // Of while
            resultString += "\r\n";
        }
        return resultString;
    }


    boolean[] tempVisitedArray;
    String resultString = "";

    public String breadthFirstTraversal(int paraStartIndex) {
        CircleObjectQueue tempQueue = new CircleObjectQueue();
        String resultString = "";
        tempVisitedArray = new boolean[numNodes];

        tempVisitedArray[paraStartIndex] = true;

        // Initialize the queue.
        // Visit before enqueue.
        tempVisitedArray[paraStartIndex] = true;
        resultString += paraStartIndex;
        tempQueue.enqueue(new Integer(paraStartIndex));

        // Now visit the rest of the graph.
        int tempIndex;
        Integer tempInteger = (Integer) tempQueue.dequeue();
        AdjacencyNode tempNode;
        while (tempInteger != null) {
            tempIndex = tempInteger.intValue();

            // Enqueue all its unvisited neighbors. The neighbors are linked
            // already.
            tempNode = headers[tempIndex].next;
            while (tempNode != null) {
                if (!tempVisitedArray[tempNode.column]) {
                    // Visit before enqueue.
                    tempVisitedArray[tempNode.column] = true;
                    resultString += tempNode.column;
                    tempQueue.enqueue(new Integer(tempNode.column));
                } // Of if
                tempNode = tempNode.next;
            } // Of for i

            // Take out one from the head.
            tempInteger = (Integer) tempQueue.dequeue();
        } // Of while

        return resultString;
    }

    public String depthFirstTraversal(int paraStartIndex) {
        ObjectStack tempStack = new ObjectStack();
        resultString = "";
        tempVisitedArray = new boolean[numNodes];

        tempVisitedArray[paraStartIndex] = true;
        resultString += paraStartIndex;
        tempStack.push(new Integer(paraStartIndex));
        System.out.println("Push " + paraStartIndex);
        System.out.println("Visited " + resultString);

        int tempIndex = paraStartIndex;
        int tempNext;
        Integer tempInteger;
        AdjacencyNode tempNode;
        while (true) {
            tempNext = -1;
            // Find an unvisited neighbor and push
            tempNode = headers[tempIndex].next;
            while (tempNode != null) {
                if (!tempVisitedArray[tempNode.column]) {
                    // Visit before enqueue.
                    tempVisitedArray[tempNode.column] = true;
                    resultString += tempNode.column;
                    tempStack.push(new Integer(tempNode.column));
                    System.out.println("Push " + tempNode.column);
                    tempNext = tempNode.column;
                    break;
                } // Of if
                tempNode = tempNode.next;
            }

            if (tempNext == -1) {
                //there is no neighbor node, pop
                tempInteger = (Integer) tempStack.pop();
                System.out.println("Pop " + tempInteger);
                if (tempStack.isEmpty()) {
                    //No unvisited neighbor。Backtracking to the last one stored in the stack
                    break;
                } else {
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
    public boolean breadthTraversal(int paraStartIndex) {
        tempVisitedArray = new boolean[numNodes];
        resultString = "";
        breadthFirstTraversal(paraStartIndex);

        for (int i = 0; i < numNodes; i++){
            if (!tempVisitedArray[i]){
                breadthFirstTraversal(i);
                return false;
            }
        }
        return true;
    }

    public boolean depthTraversal(int paraStartIndex){
        tempVisitedArray = new boolean[numNodes];
        resultString = "";
        depthFirstTraversal(paraStartIndex);

        for (int i = 0; i < numNodes; i++){
            if (!tempVisitedArray[i]){
                depthFirstTraversal(i);
                return false;
            }
        }
        return true;
    }





    public static void breadthFirstTraversalTest() {
        // Test an undirected graph.
        //int[][] tempMatrix = { { 0, 1, 1, 0 }, { 1, 0, 0, 1 }, { 1, 0, 0, 0}, { 0, 1, 0, 0} };
         //int[][] tempMatrix = { { 0, 1, 1, 0 , 0}, { 1, 0, 0, 1, 0 }, { 1, 0, 0, 1, 0}, { 0, 1, 1, 0, 0}, { 0, 0, 0, 0, 0} };
        int[][] tempMatrix = { { 0, 1, 1, 0 , 0, 0, 0}, { 1, 0, 0, 1, 0, 0, 0 }, { 1, 0, 0, 1, 0, 0, 0}, { 0, 1, 1, 0, 0, 0, 0}, { 0, 0, 0, 0, 0, 1, 1}, { 0, 0, 0, 0, 1, 0, 0}, { 0, 0, 0, 0, 0, 0, 0} };
        Graph tempGraph = new Graph(tempMatrix);
        System.out.println(tempGraph);
        AdjacencyList tempAdjList = new AdjacencyList(tempMatrix);

        String tempSequence = "";
        try {
           // tempSequence = tempAdjList.breadthFirstTraversal(2);
            tempGraph.breadthTraversal(0);
        } catch (Exception ee) {
            System.out.println(ee);
        } // Of try.

        System.out.println("The breadth first order of visit: " + tempGraph.resultString);
    }

    public static void depthFirstTraversalTest() {
        // Test an undirected graph.
        //int[][] tempMatrix = { { 0, 1, 1, 0 }, { 1, 0, 0, 1 }, { 1, 0, 0, 0}, { 0, 1, 0, 0} };
        // int[][] tempMatrix = { { 0, 1, 1, 0 , 0}, { 1, 0, 0, 1, 0 }, { 1, 0, 0, 1, 0}, { 0, 1, 1, 0, 0}, { 0, 0, 0, 0, 0} };
        int[][] tempMatrix = { { 0, 1, 1, 0 , 0, 0, 0}, { 1, 0, 0, 1, 0, 0, 0 }, { 1, 0, 0, 1, 0, 0, 0}, { 0, 1, 1, 0, 0, 0, 0}, { 0, 0, 0, 0, 0, 1, 1}, { 0, 0, 0, 0, 1, 0, 0}, { 0, 0, 0, 0, 0, 0, 0} };
        Graph tempGraph = new Graph(tempMatrix);
        System.out.println(tempGraph);
        AdjacencyList tempAdjList = new AdjacencyList(tempMatrix);

        String tempSequence = "";
        try {
            tempGraph.depthTraversal(0);
           // tempSequence = tempAdjList.depthFirstTraversal(2);
        } catch (Exception ee) {
            System.out.println(ee);
        } // Of try.

        System.out.println("The depth first order of visit: " + tempGraph.resultString);
    }


    public static void main(String args[]) {
        int[][] tempMatrix = { { 0, 1, 0 }, { 1, 0, 1 }, { 0, 1, 0 } };
        AdjacencyList tempTable = new AdjacencyList(tempMatrix);
        System.out.println("The data are:\r\n" + tempTable);

        breadthFirstTraversalTest();

        depthFirstTraversalTest();
    }

}
