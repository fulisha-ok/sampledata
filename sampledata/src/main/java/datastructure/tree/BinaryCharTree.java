package datastructure.tree;


import com.sun.xml.internal.fastinfoset.util.ValueArray;
import datastructure.queue.*;
import datastructure.stack.ObjectStack;

import java.util.Arrays;

/**
 * @Author: fulisha
 * @Date: 2023-04-09 20:12
 * @desription
 */
public class BinaryCharTree {
    /**
     * The value in char
     */
    char value;

    /**
     * The left child
     */
    BinaryCharTree leftChild;

    /**
     * The right child
     */
    BinaryCharTree rightChild;

    /**
     * The values of nodes according to breadth first traversal.
     */
    char[] valuesArray;

    /**
     * The indices in the complete binary tree.
     */
    int[] indicesArray;

    /**
     * The first constructor
     * @param paraName
     */
    public BinaryCharTree(char paraName){
        value = paraName;
        leftChild = null;
        rightChild = null;
    }

    /**
     * manually construct a tree.
     *
     * @return
     */
    public static BinaryCharTree manualConstructTree(){
        // Step 1. Construct a tree with only one node.
        BinaryCharTree resultTree = new BinaryCharTree('a');

        //Step 2. Construct all nodes. The first node is the root.
        BinaryCharTree tempTreeB = new BinaryCharTree('b');
        BinaryCharTree tempTreeC = new BinaryCharTree('c');
        BinaryCharTree tempTreeD = new BinaryCharTree('d');
        BinaryCharTree tempTreeE = new BinaryCharTree('e');
        BinaryCharTree tempTreeF = new BinaryCharTree('f');
        BinaryCharTree tempTreeG = new BinaryCharTree('g');

        // Step 3. Link all nodes.
        resultTree.leftChild = tempTreeB;
        resultTree.rightChild = tempTreeC;
        tempTreeB.rightChild = tempTreeD;
        tempTreeC.leftChild = tempTreeE;
        tempTreeD.leftChild = tempTreeF;
        tempTreeD.rightChild = tempTreeG;

        return resultTree;
    }

    /**
     * The parameters must be correct since no validity.check is undertaken.
     * @param paraDataArray  The array for data.
     * @param paraIndicesArray The array for indices.
     */
    public BinaryCharTree(char[] paraDataArray, int[] paraIndicesArray){
        // Step 1. Use a sequential list to store all nodes.
        int tempNumNodes = paraDataArray.length;
        BinaryCharTree[] tempAllNodes = new BinaryCharTree[tempNumNodes];
        for (int i = 0; i < tempNumNodes; i++){
            tempAllNodes[i] = new BinaryCharTree(paraDataArray[i]);
        }

        for (int i = 1; i < tempNumNodes; i++){
            for (int j = 0; j < i; j++){
                System.out.println("indices " + paraIndicesArray[j] + " vs. " + paraIndicesArray[i]);
                if (paraIndicesArray[i] == paraIndicesArray[j]*2 + 1){
                    // i结点是j结点的左孩子
                    tempAllNodes[j].leftChild = tempAllNodes[i];
                    System.out.println("Linking " + j + " with " + i);
                    break;
                }else if (paraIndicesArray[i] == paraIndicesArray[j] * 2 + 2){
                    // i结点是j结点的右孩子
                    tempAllNodes[j].rightChild = tempAllNodes[i];
                    System.out.println("Linking " + j + " with " + i);
                    break;
                }
            }
        }

        //Step 3. The root is the first node.
        value = tempAllNodes[0].value;
        leftChild = tempAllNodes[0].leftChild;
        rightChild = tempAllNodes[0].rightChild;
    }

    /**
     * pre-order visit
     */
    public void  preOrderVisit(){
        System.out.print("" + value + " ");

        if (leftChild != null){
            leftChild.preOrderVisit();
        }

        if (rightChild != null){
            rightChild.preOrderVisit();
        }
    }

    /**
     * in-order visit
     */
    public void  inOrderVisit(){
        if (leftChild != null){
            leftChild.inOrderVisit();
        }

        System.out.print("" + value + " ");

        if (rightChild != null) {
            rightChild.inOrderVisit();
        }
    }

    /**
     * Post-order visit.
     */
    public void postOrderVisit(){
        if (leftChild != null) {
            leftChild.postOrderVisit();
        }

        if (rightChild != null) {
            rightChild.postOrderVisit();
        }

        System.out.print("" + value + " ");
    }

    /**
     * Level-order visit.
     */
    public void levelOderVisit(){
        CircleObjectQueue tempQueue = new CircleObjectQueue();
        tempQueue.enqueue(this);

        BinaryCharTree tempTree = (BinaryCharTree) tempQueue.dequeue();
        while (tempTree != null){
            System.out.print("" + tempTree.value + " ");

            if (tempTree.leftChild != null) {
                tempQueue.enqueue(tempTree.leftChild);
            }

            if (tempTree.rightChild != null) {
                tempQueue.enqueue(tempTree.rightChild);
            }

            tempTree = (BinaryCharTree) tempQueue.dequeue();

        }
    }


    /**
     * In-order visit with stack.
     */
    public void inOrderVisitWithStack(){
        ObjectStack tempStack = new ObjectStack();
        BinaryCharTree tempNode = this;
        while (!tempStack.isEmpty() || tempNode != null){
            if (tempNode != null){
                tempStack.push(tempNode);
                tempNode = tempNode.leftChild;
            } else {
                tempNode = (BinaryCharTree)tempStack.pop();
                System.out.print("" + tempNode.value + " ");
                tempNode = tempNode.rightChild;
            }
        }
    }

    /**
     * Pre-order visit with stack.
     */
    public void preOrderVisitWithStack() {
        ObjectStack tempStack = new ObjectStack();
        BinaryCharTree tempNode = this;
        while (!tempStack.isEmpty() || tempNode != null) {
            if (tempNode != null) {
                System.out.print("" + tempNode.value + " ");
                tempStack.push(tempNode);
                tempNode = tempNode.leftChild;
            } else {
                tempNode = (BinaryCharTree) tempStack.pop();
                tempNode = tempNode.rightChild;
            }
        }
    }

    /**
     * Post-order visit with stack.
     */
    public void postOrderVisitWithStack() {
        ObjectStack tempStack = new ObjectStack();
        BinaryCharTree tempNode = this;
        ObjectStack tempOutputStack = new ObjectStack();

        while (!tempStack.isEmpty() || tempNode != null) {
            if (tempNode != null) {
                //Store for output.
                tempOutputStack.push(new Character(tempNode.value));
                tempStack.push(tempNode);
                tempNode = tempNode.rightChild;
            } else {
                tempNode = (BinaryCharTree) tempStack.pop();
                tempNode = tempNode.leftChild;
            }
        }

        //Now reverse output.
        while (!tempOutputStack.isEmpty()) {
            System.out.print("" + tempOutputStack.pop() + " ");
        }
    }



    public int getDepth(){
        // It is a leaf.
        if ((leftChild == null) && (rightChild == null)) {
            return 1;
        }

        // The depth of the left child.
        int tempLeftDepth = 0;
        if (leftChild != null){
            tempLeftDepth = leftChild.getDepth();
        }

        int tempRighDepth = 0;
        if (rightChild != null){
            tempRighDepth = rightChild.getDepth();
        }

        if (tempLeftDepth >= tempRighDepth) {
            return tempLeftDepth + 1;
        }else {
            return tempRighDepth + 1;
        }
    }

    /**
     * get the number of nodes
     * @return
     */
    public int getNumNodes(){
        if (leftChild == null && rightChild == null){
            return  1;
        }

        int tempLeftNodes = 0;
        if (leftChild != null){
            tempLeftNodes = leftChild.getNumNodes();
        }

        int tempRightNodes = 0;
        if (rightChild != null) {
            tempRightNodes = rightChild.getNumNodes();
        }

        return tempLeftNodes + tempRightNodes + 1;
    }

    public void  toDataArrays(){
        int tempLength = getNumNodes();

        valuesArray = new char[tempLength];
        indicesArray = new int[tempLength];
        int i = 0;

        //Traverse and convert at the same time.
        CircleObjectQueue tempQueue = new CircleObjectQueue();
        tempQueue.enqueue(this);
        CircleIntQueue tempIntQueue = new CircleIntQueue();
        tempIntQueue.enqueue(0);

        BinaryCharTree tempTree = (BinaryCharTree) tempQueue.dequeue();
        int tempIndex = tempIntQueue.dequeue();
        while (tempTree != null){
            valuesArray[i] = tempTree.value;
            indicesArray[i] = tempIndex;
            i++;

            if (tempTree.leftChild != null) {
                tempQueue.enqueue(tempTree.leftChild);
                tempIntQueue.enqueue(tempIndex * 2 + 1);
            }

            if (tempTree.rightChild != null) {
                tempQueue.enqueue(tempTree.rightChild);
                tempIntQueue.enqueue(tempIndex * 2 + 2);
            }

            tempTree = (BinaryCharTree) tempQueue.dequeue();
            tempIndex = tempIntQueue.dequeue();
        }

    }

    /**
     * Covert the tree to data arrays, including a char array and an int array
     * The results are stored in two member variables
     */
    public void toDataArraysObjectQueue(){
        int tempLength = getNumNodes();

        valuesArray = new char[tempLength];
        indicesArray = new int[tempLength];
        int i = 0;

        CircleObjectQueue tempQueue = new CircleObjectQueue();
        tempQueue.enqueue(this);
        CircleObjectQueue tempIntQueue = new CircleObjectQueue();
        Integer tempIndexInteger = 0;  // 自动装箱，等价于 Integer tempIndexInteger = Integer.valueOf(0);
        tempIntQueue.enqueue(tempIndexInteger);

        BinaryCharTree tempTree = (BinaryCharTree) tempQueue.dequeue();
        int tempIndex = (int) tempIntQueue.dequeue();
        System.out.println("tempIndex = " + tempIndex);
        while (tempTree != null){
            valuesArray[i] = tempTree.value;
            indicesArray[i] = tempIndex;
            i++;

            if (tempTree.leftChild != null){
                tempQueue.enqueue(tempTree.leftChild);
                tempIntQueue.enqueue(Integer.valueOf(tempIndex * 2 + 1));
            }

            if (tempTree.rightChild != null) {
                tempQueue.enqueue(tempTree.rightChild);
                tempIntQueue.enqueue(Integer.valueOf(tempIndex * 2 + 2));
            }

            tempTree = (BinaryCharTree) tempQueue.dequeue();
            if (tempTree == null) {
                break;
            }


            tempIndex = (int) tempIntQueue.dequeue(); //等价于 ((Integer) tempIntQueue.dequeue()).intValue();
        }

    }

    public static void main(String args[]) {
        BinaryCharTree tempTree = manualConstructTree();
        System.out.println("\r\nPreorder visit:");
        tempTree.preOrderVisit();
        System.out.println("\r\nIn-order visit:");
        tempTree.inOrderVisit();
        System.out.println("\r\nPost-order visit:");
        tempTree.postOrderVisit();
        System.out.println("\r\nLevel-order visit:");
        tempTree.levelOderVisit();

        System.out.println("\r\n\r\nThe depth is: " + tempTree.getDepth());
        System.out.println("The number of nodes is: " + tempTree.getNumNodes());

        tempTree.toDataArrays();
        System.out.println("The values are: " + Arrays.toString(tempTree.valuesArray));
        System.out.println("The indices are: " + Arrays.toString(tempTree.indicesArray));

        tempTree.toDataArraysObjectQueue();
        System.out.println("Only object queue.");
        System.out.println("The values are: " + Arrays.toString(tempTree.valuesArray));
        System.out.println("The indices are: " + Arrays.toString(tempTree.indicesArray));

        System.out.println("*********************************************");
        //char[] tempCharArray = {'A', 'B', 'C', 'D', 'E', 'F'};
        //int[] tempIndicesArray = {0, 1, 2, 4, 5, 12};
        char[] tempCharArray = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        int[] tempIndicesArray = {0, 1, 2, 4, 5, 9, 10};
        BinaryCharTree tempTree2 = new BinaryCharTree(tempCharArray, tempIndicesArray);

        System.out.println("\r\nPreorder visit:");
        tempTree2.preOrderVisit();
        System.out.println("\r\nIn-order visit:");
        tempTree2.inOrderVisit();
        System.out.println("\r\nPost-order visit:");
        tempTree2.postOrderVisit();

        System.out.println("\r\nIn-order visit with stack:");
        tempTree2.inOrderVisitWithStack();
        System.out.println("\r\nPre-order visit with stack:");
        tempTree2.preOrderVisitWithStack();
        System.out.println("\r\nPost-order visit with stack:");
        tempTree2.postOrderVisitWithStack();
    }



}
