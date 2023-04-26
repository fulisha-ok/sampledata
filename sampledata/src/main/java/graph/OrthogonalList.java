package graph;

/**
 * @author： fulisha
 * @date： 2023/4/25 15:44
 * @description：Orthogonal List for directed graph.
 */
public class OrthogonalList {
    class OrthogonalNode {
        int row;
        int column;
        /**
         * The next out node.
         */
        OrthogonalNode nextOut;

        /**
         * The next in node.
         */
        OrthogonalNode nextIn;

        /**
         * the first constructor
         * @param paraRow
         * @param paraColumn
         */
        public OrthogonalNode(int paraRow, int paraColumn) {
            row = paraRow;
            column = paraColumn;
            nextOut = null;
            nextIn = null;
        }

        @Override
        public String toString() {
            String resultString = "";
            resultString = "row: " + row + " column: " + column;
            return resultString;
        }
    }

    /**
     * The number of nodes. This member variable may be redundant since it is always  equal to headers.length
     */
    int numNodes;

    /**
     * The headers for each row.
     */
    OrthogonalNode[] headers;

    public OrthogonalList(int[][] paraMatrix) {
        numNodes = paraMatrix.length;

        // Step 1. Initialize. The data in the headers are not meaningful.
        OrthogonalNode tempPreviousNode, tempNode;
        headers = new OrthogonalNode[numNodes];

        // Step 2. Link to its out nodes.
        for (int i = 0; i < numNodes; i++) {
            headers[i] = new OrthogonalNode(i, -1);
            tempPreviousNode = headers[i];
            for (int j = 0; j < numNodes; j++) {
                if (paraMatrix[i][j] == 0) {
                    continue;
                }
                tempNode = new OrthogonalNode(i, j);

                tempPreviousNode.nextOut = tempNode;
                tempPreviousNode = tempNode;
            }
        }

        //Step3 Link to its in nodes. This step is harder
        OrthogonalNode[] tempColumnNodes = new OrthogonalNode[numNodes];
        for (int i = 0; i < numNodes; i++) {
            tempColumnNodes[i] = headers[i];
        }
        for (int i = 0; i < numNodes; i++) {
            tempNode = headers[i].nextOut;
            while (tempNode != null) {
                tempColumnNodes[tempNode.column].nextIn = tempNode;
                tempColumnNodes[tempNode.column] = tempNode;

                tempNode = tempNode.nextOut;
            }
        }
    }

    @Override
    public String toString() {
        String resultString = "Out arcs: ";

        OrthogonalNode tempNode;
        for (int i = 0; i < numNodes; i++) {
            tempNode = headers[i].nextOut;
            while (tempNode != null) {
                resultString += " (" + tempNode.row + ", " + tempNode.column + ")";
                tempNode = tempNode.nextOut;
            }
            resultString += "\r\n";
        }
        resultString += "\r\nIn arcs: ";

        for (int i = 0; i < numNodes; i++) {
            tempNode = headers[i].nextIn;

            while (tempNode != null) {
                resultString += " (" + tempNode.row + ", " + tempNode.column + ")";
                tempNode = tempNode.nextIn;
            }
            resultString += "\r\n";
        }

        return resultString;
    }

    public static void main(String[] args) {
        int[] a = new int[10];
        int[] b = new int[10];
        int[] c = new int[10];
        b = a;
        b[0] = 1;
        b[1] = 2;
        b[3] = 3;
        for (int i = 0; i < 3; i++) {
            c[i] = b[i];
        }
        System.out.println("a数组引用地址 " + a);
        System.out.println("b数组引用地址 " + b);
        System.out.println("c数组引用地址 " + c);

        int[][] tempMatrix = { { 0, 1, 0, 0 }, { 0, 0, 0, 1 }, { 1, 0, 0, 0 }, { 0, 1, 1, 0 } };
        OrthogonalList tempList = new OrthogonalList(tempMatrix);
        System.out.println("The data are:\r\n" + tempList);
    }


}
