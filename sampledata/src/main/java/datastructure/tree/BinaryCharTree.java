package datastructure.tree;


import com.sun.xml.internal.fastinfoset.util.ValueArray;
import datastructure.queue.*;

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
    }



}
