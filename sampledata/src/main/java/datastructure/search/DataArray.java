package datastructure.search;

import java.io.File;

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

    @Override
    public String toString(){
        String resultString = "I am a data array with " + length + " items.\r\n";
        for (int i = 0; i < length; i++) {
            resultString += i + "";
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
        String[] tempContents = { "if", "then", "else", "switch", "case", "for", "while" };
        DataArray tempDataArray = new DataArray(tempSortedKeys, tempContents);

        System.out.println(tempDataArray);

        System.out.println("Search result of 10 is: " + tempDataArray.binarySearch(10));
        System.out.println("Search result of 5 is: " + tempDataArray.binarySearch(5));
        System.out.println("Search result of 4 is: " + tempDataArray.binarySearch(4));
    }

    public static void main(String args[]) {
        System.out.println("\r\n-------sequentialSearchTest-------");
        sequentialSearchTest();

        System.out.println("\r\n-------binarySearchTest-------");
        binarySearchTest();
    }



}
