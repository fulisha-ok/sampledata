package datastructure.tree;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author： fulisha
 * @date： 2023/4/14 9:22
 * @description：
 */
public class Huffman {
    /**
     * An inner class for Huffman nodes.
     */
    class HuffmanNode {
        /**
         * The char. Only valid for leaf nodes.
         */
        char character;

        /**
         * Weight. It can also be double.
         */
        int weight;

        /**
         * The left child.
         */
        HuffmanNode leftChild;

        /**
         * The right child.
         */
        HuffmanNode rightChild;

        /**
         * The parent. It helps constructing the Huffman code of each character.
         */
        HuffmanNode parent;

        /**
         * The first constructor
         */
        public HuffmanNode(char paraCharacter, int paraWeight, HuffmanNode paraLeftChild,
                           HuffmanNode paraRightChild, HuffmanNode paraParent) {
            character = paraCharacter;
            weight = paraWeight;
            leftChild = paraLeftChild;
            rightChild = paraRightChild;
            parent = paraParent;
        }

    }

    /**
     * The number of characters. 256 for ASCII.
     */
    public static final int NUM_CHARS = 256;

    /**
     *  The input text. It is stored in a string for simplicity.
     */
    String inputText;

    /**
     * The length of the alphabet, also the number of leaves.
     */
    int alphabetLength;

    /**
     * The alphabet.
     */
    char[] alphabet;

    /**
     * The count of chars. The length is 2 * alphabetLength - 1 to include non-leaf nodes.
     */
    int[] charCounts;

    /**
     * The mapping of chars to the indices in the alphabet.
     */
    int[] charMapping;

    /**
     * Codes for each char in the alphabet. It should have the same length as alphabet
     */
    String[] huffmanCodes;

    /**
     * All nodes. The last node is the root.
     */
    HuffmanNode[] nodes;

    public Huffman(String paraFilename){
        charMapping = new int[NUM_CHARS];
        readText((paraFilename));

    }

    /**
     * Read text
     * @param paraFilename  The text filename.
     */
    public void readText(String paraFilename){
        try {
            inputText = Files.newBufferedReader(Paths.get(paraFilename), StandardCharsets.UTF_8)
                    .lines().collect(Collectors.joining("\n"));
        } catch (Exception ee) {
            System.out.println(ee);
            System.exit(0);
        }

        System.out.println("The text is:\r\n" + inputText);
    }

    /**
     * Construct the alphabet. The results are stored in the member variables charMapping and alphabet.
     */
    public void constructAlphabet(){
        //initialize
        Arrays.fill(charMapping, -1);

        // The count for each char. At most NUM_CHARS chars.
        int[] tempCharCounts = new int[NUM_CHARS];

        //The index of the char in the ASCII charset
        int tempCharIndex;

        char tempChar;
        for (int i = 0; i < inputText.length(); i++){
            tempChar = inputText.charAt(i);
            tempCharIndex = (int)tempChar;

            System.out.print("" + tempCharIndex + " ");
            tempCharCounts[tempCharIndex]++;
        }

        // Step 2. Scan to determine the size of the alphabet
        alphabetLength = 0;
        for (int i = 0; i < 255; i++){
            if (tempCharCounts[i] > 0){
                alphabetLength++;
            }
        }

        //step3. Compress to the alphabet
        alphabet = new char[alphabetLength];
        charCounts = new int[2 * alphabetLength - 1];

        int tempCounter = 0;
        for (int i = 0; i < NUM_CHARS; i++) {
            if (tempCharCounts[i] > 0) {
                alphabet[tempCounter] = (char) i;
                charCounts[tempCounter] = tempCharCounts[i];
                charMapping[i] = tempCounter;
                tempCounter++;
            }
        }

        System.out.println("The alphabet is: " + Arrays.toString(alphabet));
        System.out.println("Their counts are: " + Arrays.toString(charCounts));
        System.out.println("The char mappings are: " + Arrays.toString(charMapping));

    }


    /**
     * Construct the tree.
     */
    public void constructTree(){
        // Step 1. Allocate space.
        nodes = new HuffmanNode[alphabetLength * 2 - 1];
        boolean[] tempProcessed = new boolean[alphabetLength * 2 - 1];

        // Step 2. Initialize leaves.
        for (int i = 0; i < alphabetLength; i++) {
            nodes[i] = new HuffmanNode(alphabet[i], charCounts[i], null, null, null);
        }

        // Step 3. Construct the tree.
        int tempLeft, tempRight, tempMinimal;
        for (int i = alphabetLength; i < 2 * alphabetLength - 1; i++) {
            // Step 3.1 Select the first minimal as the left child.
            tempLeft = -1;
            tempMinimal = Integer.MAX_VALUE;
            for (int j = 0; j < i; j++) {
                if (tempProcessed[j]) {
                    continue;
                }

                if (tempMinimal > charCounts[j]) {
                    tempMinimal = charCounts[j];
                    tempLeft = j;
                }
            }
            tempProcessed[tempLeft] = true;

            // Step 3.2 Select the second minimal as the right child.
            tempRight = -1;
            tempMinimal = Integer.MAX_VALUE;
            for (int j = 0; j < i; j++) {
                if (tempProcessed[j]) {
                    continue;
                }

                if (tempMinimal > charCounts[j]) {
                    tempMinimal = charCounts[j];
                    tempRight = j;
                }
            }
            tempProcessed[tempRight] = true;
            System.out.println("Selecting " + tempLeft + " and " + tempRight + "========== the value:" + charCounts[tempLeft] + " and " + charCounts[tempRight]);

            // Step 3.3 Construct the new node.
            charCounts[i] = charCounts[tempLeft] + charCounts[tempRight];
            nodes[i] = new HuffmanNode('*', charCounts[i], nodes[tempLeft], nodes[tempRight], null);

            // Step 3.4 Link with children.
            nodes[tempLeft].parent = nodes[i];
            nodes[tempRight].parent = nodes[i];
            System.out.println("The children of " + i + " are " + tempLeft + " and " + tempRight+ "========== the value:" + charCounts[i] +" are "  + charCounts[tempLeft] + " and " + charCounts[tempRight]);
        }
        System.out.println("Their counts are: " + Arrays.toString(charCounts));
    }

    /**
     * Get the root of the binary tree.
     * @return
     */
    public HuffmanNode getRoot() {
        return nodes[nodes.length - 1];
    }

    /**
     * Pre-order visit.
     * @param paraNode
     */
    public void preOrderVisit(HuffmanNode paraNode) {
        System.out.print("(" + paraNode.character + ", " + paraNode.weight + ") ");

        if (paraNode.leftChild != null) {
            preOrderVisit(paraNode.leftChild);
        }

        if (paraNode.rightChild != null) {
            preOrderVisit(paraNode.rightChild);
        }
    }

    /**
     * Generate codes for each character in the alphabet.
     */
    public void  generateCodes() {
        huffmanCodes = new String[alphabetLength];
        HuffmanNode tempNode;
        // eg: nodes = new HuffmanNode[alphabetLength * 2 - 1];  alphabetLength is this chars length: [a, d, h, s, u]
        for (int i = 0; i < alphabetLength; i++) {
            tempNode = nodes[i];
            String tempCharCode = "";
            while (tempNode.parent != null) {
                if (tempNode == tempNode.parent.leftChild) {
                    tempCharCode = "0" + tempCharCode;
                } else {
                    tempCharCode = "1" + tempCharCode;
                }
                tempNode = tempNode.parent;
            }

            huffmanCodes[i] = tempCharCode;
            System.out.println("The code of " + alphabet[i] + " is " + tempCharCode);
        }
    }

    /**
     * Encode the given string.
     * @param paraString
     * @return
     */
    public String coding(String paraString) {
        String resultCodeString = "";
        int tempIndex;
        for (int i = 0; i < paraString.length(); i++) {
            tempIndex = charMapping[(int) paraString.charAt(i)];

            resultCodeString += huffmanCodes[tempIndex];
        }
        return resultCodeString;
    }

    /**
     *  Decode the given string.
     * @param paraString  The given string.
     * @return
     */
    public String decoding(String paraString){
        String resultCodeString = "";
        HuffmanNode tempNode = getRoot();

        for (int i = 0; i < paraString.length(); i++) {
            if (paraString.charAt(i) == '0') {
                tempNode = tempNode.leftChild;
                System.out.println(tempNode);
            } else {
                tempNode = tempNode.rightChild;
                System.out.println(tempNode);
            }

            if (tempNode.leftChild == null) {
                System.out.println("Decode one:" + tempNode.character);
                // Decode one char.
                resultCodeString += tempNode.character;

                // Return to the root.
                tempNode = getRoot();
            }
        }
        return resultCodeString;
    }



    public static void main(String[] args) {
        Huffman tempHuffman = new Huffman("D:/fulisha/fulisha.txt");
        tempHuffman.constructAlphabet();

        tempHuffman.constructTree();

        HuffmanNode tempRoot = tempHuffman.getRoot();
        System.out.println("The root is: " + tempRoot);
        System.out.println("Preorder visit:");
        tempHuffman.preOrderVisit(tempHuffman.getRoot());
        System.out.println("\r\n");

        tempHuffman.generateCodes();

        String tempCoded = tempHuffman.coding("hdashuhaasahssa");
        System.out.println("Coded: " + tempCoded);
        String tempDecoded = tempHuffman.decoding(tempCoded);
        System.out.println("Decoded: " + tempDecoded);

    }

}
