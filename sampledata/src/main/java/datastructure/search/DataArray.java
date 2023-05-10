package datastructure.search;

import java.io.File;
import java.util.HashMap;

/**
 * @author： fulisha
 * @date： 2023/5/4 9:49
 * @description：
 */
public class DataArray {
    /**
     * an inner class for data nodes. The text book usually use an int value to represent the data
     * I would like to use a key-value pair instead
     */
    class DataNode{
        int key;

        String content;

        DataNode(int paraKey, String paraContent) {
            key = paraKey;
            content = paraContent;
        }

        @Override
        public String toString() {
            return "(" + key + ", " + content + ") ";
        }
    }

    /**
     * the data array
     */
    DataNode[] data;

    /**
     * the length of the data array
     */
    int length;

    public DataArray(int[] paraKeyArray, String[] paraContentArray) {
        length = paraContentArray.length;
        data = new DataNode[length];

        for (int i = 0; i < length; i++) {
            data[i] = new DataNode(paraKeyArray[i], paraContentArray[i]);
        }
    }

    /**
     *For Hash code only. It is assumed that paraKeyArray.length <= paraLength.
     * @param paraKeyArray
     * @param paraContentArray
     * @param paraLength
     */
    public DataArray(int[] paraKeyArray, String[] paraContentArray, int paraLength) {
        // step1.Initialize
        length = paraLength;
        data = new DataNode[length];
        for (int i = 0; i < length; i++) {
            data[i] = null;
        }

        //step2. fill the data
        int tempPosition;
        for (int i = 0; i < paraKeyArray.length; i++) {
            //Hash
            tempPosition = paraKeyArray[i] % paraLength;

            //Find an empty position
            while (data[tempPosition] != null) {
                tempPosition = (tempPosition + 1) % paraLength;
                System.out.println("Collision, move forward for key " + paraKeyArray[i]);
            }

            data[tempPosition] = new DataNode(paraKeyArray[i], paraContentArray[i]);
        }
    }

    public String hashSearch(int paraKey) {
        int tempPosition = paraKey % length;
        while (data[tempPosition] != null) {
            if (data[tempPosition].key == paraKey) {
                return data[tempPosition].content;
            }
            System.out.println("Not this one for " + paraKey);
            tempPosition = (tempPosition + 1) % length;
        }
        return "null";
    }

    public static void hashSearchTest() {
        int[] tempUnsortedKeys = { 16, 33, 38, 69, 57, 95, 86 };
        String[] tempContents = { "if", "then", "else", "switch", "case", "for", "while" };
        DataArray tempDataArray = new DataArray(tempUnsortedKeys, tempContents, 19);
        System.out.println(tempDataArray);

        System.out.println("Search result of 95 is: " + tempDataArray.hashSearch(95));
        System.out.println("Search result of 38 is: " + tempDataArray.hashSearch(38));
        System.out.println("Search result of 57 is: " + tempDataArray.hashSearch(57));
        System.out.println("Search result of 4 is: " + tempDataArray.hashSearch(4));
    }

    @Override
    public String toString(){
        String resultString = "I am a data array with " + length + " items.\r\n";
        for (int i = 0; i < length; i++) {
            resultString += data[i] + " ";
        }
        return resultString;
    }

    public String sequentialSearch(int paraKey) {
        data[0].key = paraKey;

        int i;
        for (i = length-1; data[i].key != paraKey; i--) {
            ;
        }
        return data[i].content;
    }
    public static void sequentialSearchTest() {
        int[] tempUnsortedKeys = { -1, 5, 3, 6, 10, 7, 1, 9 };
        String[] tempContents = { "null", "if", "then", "else", "switch", "case", "for", "while" };
        DataArray tempDataArray = new DataArray(tempUnsortedKeys, tempContents);

        System.out.println(tempDataArray);

        System.out.println("Search result of 10 is: " + tempDataArray.sequentialSearch(10));
        System.out.println("Search result of 5 is: " + tempDataArray.sequentialSearch(5));
        System.out.println("Search result of 4 is: " + tempDataArray.sequentialSearch(4));
    }

    public String binarySearch(int paraKey) {
        int tempLeft = 0;
        int tempRight = length-1;
        int tempMiddle = (tempLeft + tempRight) / 2;

        while (tempLeft <= tempRight) {
            tempMiddle = (tempLeft + tempRight) / 2;
            if (data[tempMiddle].key == paraKey) {
                return data[tempMiddle].content;
            }else if (data[tempMiddle].key <= paraKey) {
                tempLeft = tempMiddle + 1;
            }else {
                tempRight = tempMiddle - 1;
            }
        }

        return "null";
    }

    public static void binarySearchTest() {
        int[] tempSortedKeys = { 1, 3, 5, 6, 7, 9, 10 };
        String[] tempContents = { "if", "then", "el se", "switch", "case", "for", "while" };
        DataArray tempDataArray = new DataArray(tempSortedKeys, tempContents);

        System.out.println(tempDataArray);

        System.out.println("Search result of 10 is: " + tempDataArray.binarySearch(10));
        System.out.println("Search result of 5 is: " + tempDataArray.binarySearch(5));
        System.out.println("Search result of 4 is: " + tempDataArray.binarySearch(4));
    }

    /**
     * insertion sort.
     * data[0] dose not store a valid  data. data[0].key should be smaller than any valid key
     */
    public void insertionSort() {
        DataNode tempNode;
        int j;
        for (int i = 2; i < length; i++) {
            tempNode = data[i];
            for (j = i-1; data[j].key > tempNode.key; j--) {
                data[j+1] = data[j];
            }

            data[j+1] = tempNode;

            System.out.println("Round " + (i - 1));
            System.out.println(this);
        }
    }

    public static void insertionSortTest() {
        int[] tempUnsortedKeys = { -100, 5, 3, 6, 10, 7, 1, 9 };
        String[] tempContents = { "null", "if", "then", "else", "switch", "case", "for", "while" };
        DataArray tempDataArray = new DataArray(tempUnsortedKeys, tempContents);

        System.out.println(tempDataArray);

        tempDataArray.insertionSort();
        System.out.println("Result\r\n" + tempDataArray);
    }

    /**
     * shell sort.we do not use sentries here because too many of them are needed
     */
    public void shellSort() {
        DataNode tempNode;
        int[] tempJumpArray = {5, 3, 1};
        int tempJump;
        int p;
        for (int i = 0; i < tempJumpArray.length; i++) {
            tempJump = tempJumpArray[i];
            for (int j = 0; j < tempJump; j++) {
                for (int k = j + tempJump; k < length; k += tempJump) {
                    tempNode = data[k];
                    // find the position to insert. at the same time, move other nodes
                    for (p = k - tempJump; p >= 0; p -= tempJump) {
                        if (data[p].key > tempNode.key) {
                            data[p+tempJump] = data[p];
                        } else {
                            break;
                        }
                    }
                    data[p + tempJump] = tempNode;
                }
            }
            System.out.println("Round " + i);
            System.out.println(this);
        }
    }

    public static void shellSortTest() {
        int[] tempUnsortedKeys = { 5, 3, 6, 10, 7, 1, 9, 12, 8, 4 };
        String[] tempContents = { "if", "then", "else", "switch", "case", "for", "while", "throw", "until", "do" };
        DataArray tempDataArray = new DataArray(tempUnsortedKeys, tempContents);

        System.out.println(tempDataArray);

        tempDataArray.shellSort();
        System.out.println("Result\r\n" + tempDataArray);
    }

    /**
     * bubble sort
     */
    public void bubbleSort() {
        boolean tempSwapped;
        DataNode tempNode;
        for (int i = length - 1; i > 0; i--) {
            tempSwapped = false;
            for (int j = 0; j < i; j++) {
                if (data[j].key > data[j+1].key) {
                    tempNode = data[j+1];
                    data[j+1] = data[j];
                    data[j] = tempNode;

                    tempSwapped = true;
                }
            }

            if (!tempSwapped) {
                System.out.println("Premature");
                break;
            }

            System.out.println("Round " + (length - i));
            System.out.println(this);
        }
    }

    public static void bubbleSortTest() {
        int[] tempUnsortedKeys = { 1, 3, 6, 10, 7, 5, 9 };
        String[] tempContents = { "if", "then", "else", "switch", "case", "for", "while" };
        DataArray tempDataArray = new DataArray(tempUnsortedKeys, tempContents);

        System.out.println(tempDataArray);

        tempDataArray.bubbleSort();
        System.out.println("Result\r\n" + tempDataArray);
    }

    /**
     * Quick sort recursively.
     * @param paraStart The start index.
     * @param paraEnd The end index
     */
    public void quickSortRecursive(int paraStart, int paraEnd) {
        // nothing to sort
        if (paraStart >= paraEnd) {
            return;
        }
        int tempPivot = data[paraEnd].key;
        DataNode tempNodeForSwap;

        int tempLeft = paraStart;
        int tempRight = paraEnd - 1;

        //find the position for the pivot. at the same time move smaller elements to the left and bigger one to the right
        while (true) {
            while ((data[tempLeft].key < tempPivot) && tempLeft < tempRight) {
                tempLeft++;
            }
            while ((data[tempRight].key) > tempPivot && tempLeft < tempRight) {
                tempRight--;
            }

            if (tempLeft < tempRight) {
                //swap
                System.out.println("Swapping " + tempLeft + " and " + tempRight);
                tempNodeForSwap = data[tempLeft];
                data[tempLeft] = data[tempRight];
                data[tempRight] = tempNodeForSwap;
            } else {
                break;
            }
        }
        // Swap
        if (data[tempLeft].key > tempPivot) {
            tempNodeForSwap = data[paraEnd];
            data[paraEnd] = data[tempLeft];
            data[tempLeft] = tempNodeForSwap;
        } else {
            tempLeft++;
        }

        System.out.print("From " + paraStart + " to " + paraEnd + ": ");
        System.out.println(this);
        quickSortRecursive(paraStart, tempLeft - 1);
        quickSortRecursive(tempLeft + 1, paraEnd);
    }

    public void quickSort() {
        quickSortRecursive(0, length-1);
    }

    /**
     * Test the method.
     */
    public static void quickSortTest() {
        int[] tempUnsortedKeys = { 1, 3, 12, 10, 5, 7, 9 };
        //int[] tempUnsortedKeys = { 25, 45, 55, 15, 20, 50 };
        String[] tempContents = { "if", "then", "else", "switch", "case", "for", "while" };
        //String[] tempContents = { "if", "then", "else", "switch", "case", "for" };
        DataArray tempDataArray = new DataArray(tempUnsortedKeys, tempContents);

        System.out.println(tempDataArray);

        tempDataArray.quickSort();
        System.out.println("Result\r\n" + tempDataArray);
    }

    public void selectionSort() {
        DataNode tempNode;
        int tempIndexForSmallest;

        for (int i = 0; i < length -1; i++) {
            //initialize
            tempNode = data[i];
            tempIndexForSmallest = i;
            for (int j = i + 1; j < length; j++) {
                if (data[j].key < tempNode.key) {
                    tempNode = data[j];
                    tempIndexForSmallest = j;
                }
            }

            // Change the selected one with the current one.
            data[tempIndexForSmallest] = data[i];
            data[i] = tempNode;
        }
    }

    public static void selectionSortTest() {
        int[] tempUnsortedKeys = { 5, 3, 6, 10, 7, 1, 9 };
        String[] tempContents = { "if", "then", "else", "switch", "case", "for", "while" };
        DataArray tempDataArray = new DataArray(tempUnsortedKeys, tempContents);

        System.out.println(tempDataArray);

        tempDataArray.selectionSort();
        System.out.println("Result\r\n" + tempDataArray);
    }

    /**
     * heap sort. Maybe the most difficult sorting algorithm.
     */
    public void heapSort() {
        DataNode tempNode;
        //step1 Construct the initial heap
        for (int i = length/2 -1 ; i >= 0; i--){
            adjustHeap(i, length);
        }
        System.out.println("The initial heap: " + this + "\r\n");

        //step2 swap and reconstruct
        for (int i = length -1; i > 0; i--) {
            tempNode = data[0];
            data[0] = data[i];
            data[i] = tempNode;

            adjustHeap(0, i);
            System.out.println("Round " + (length - i) + ": " + this);
        }



    }

    /**
     * Adjust the heap
     * @param paraStart the start of the index
     * @param paraLength the length of the adjusted sequence
     */
    public void adjustHeap(int paraStart, int paraLength) {
        DataNode tempNode = data[paraStart];
        int tempParent = paraStart;
        int tempKey = data[paraStart].key;

        for (int tempChild = paraStart*2 + 1; tempChild <  paraLength; tempChild = tempChild*2 + 1) {
            //the right child is bigger
            if (tempChild + 1 < paraLength) {
                if (data[tempChild].key < data[tempChild+1].key) {
                    tempChild++;
                }
            }

            System.out.println("The parent position is " + tempParent + " and the child is " + tempChild);
            if (tempKey < data[tempChild].key) {
                // the child is bigger
                data[tempParent] = data[tempChild];
                System.out.println("Move " + data[tempChild].key + " to position " + tempParent);
                tempParent = tempChild;
            } else {
                break;
            }
        }

        data[tempParent] = tempNode;
        System.out.println("Adjust " + paraStart + " to " + paraLength + ": " + this);
    }

    public static void heapSortTest() {
        //int[] tempUnsortedKeys = { 5, 3, 6, 10, 7, 1, 9 };
        int[] tempUnsortedKeys = { 53, 17, 78, 9, 45, 65, 87, 32};
        //String[] tempContents = { "if", "then", "else", "switch", "case", "for", "while" };
        String[] tempContents = { "if", "then", "else", "switch", "case", "for", "while" , "break"};
        DataArray tempDataArray = new DataArray(tempUnsortedKeys, tempContents);

        System.out.println(tempDataArray);

        tempDataArray.heapSort();
        System.out.println("Result\r\n" + tempDataArray);
    }

    public static void main(String[] args) {
        System.out.println("\r\n-------sequentialSearchTest-------");
        sequentialSearchTest();

        System.out.println("\r\n-------binarySearchTest-------");
        binarySearchTest();

        System.out.println("\r\n-------hashSearchTest-------");
        hashSearchTest();

        System.out.println("\r\n-------insertionSortTest-------");
        insertionSortTest();

        System.out.println("\r\n-------shellSortTest-------");
        shellSortTest();

        System.out.println("\r\n-------bubbleSortTest-------");
        bubbleSortTest();

        System.out.println("\r\n-------quickSortTest-------");
        quickSortTest();

        System.out.println("\r\n-------selectionSortTest-------");
        selectionSortTest();

        System.out.println("\r\n-------heapSortTest-------");
        heapSortTest();
    }




}
