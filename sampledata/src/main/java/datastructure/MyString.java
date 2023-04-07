package main.java.datastructure;

import sun.applet.Main;

import java.time.Year;

/**
 * @Author: fulisha
 * @Email: 376414521@qq.com
 * @desription
 */
public class MyString {
    /**
     *  The maximal length.
     */
    public static final int MAX_LENGTH = 10;

    /**
     * The actual length.
     */
    int length;

    /**
     * The data.
     */
    char[] data;

    /**
     * Construct an empty char array.
     */
    public MyString(){
        length = 0;
        data = new char[MAX_LENGTH];
    }

    /**
     *Construct using a system defined string.
     * @param paraString The given string. Its length should not exceed MAX_LENGTH - 1.
     */
    public MyString(String paraString){
        data = new char[MAX_LENGTH];
        length = paraString.length();

        for (int i = 0; i < length; i++){
            data[i] = paraString.charAt(i);
        }
    }

    public String toString(){
        String resultString = "";

        for (int i = 0; i < length; i++){
            resultString += data[i];
        }

        return resultString;
    }

    /**
     * Locate the position of a substring.
     * @param paraString The given substring.
     * @return The first position. -1 for no matching.
     */
    public int locate(MyString paraString){
        boolean tempMatch = false;

        for (int i = 0; i < length - paraString.length + 1; i++){
            tempMatch = true;

            for (int j = 0; j < paraString.length; j++){
                if (data[i+j] != paraString.data[j]){
                    tempMatch = false;
                    break;
                }
            }
            if (tempMatch){
                return i;
            }
        }

        return -1;
    }

    /**
     * Get a substring
     * @param paraStartPosition The start position in the original string.
     * @param paraLength  The length of the new string.
     * @return The first position. -1 for no matching.
     */
    public MyString substring(int paraStartPosition, int paraLength){
        if (paraStartPosition + paraLength > length){
            System.out.println("The bound is exceeded.");
            return null;
        }

        MyString resultMyString = new MyString();
        resultMyString.length = paraLength;
        for (int i = 0; i < paraLength; i++){
            resultMyString.data[i] = data[paraStartPosition + i];
        }

        return resultMyString;
    }

    public static void main(String args[]) {
        MyString tempFirstString = new MyString("I like ik.");
        MyString tempSecondString = new MyString("ik");
        int tempPosition = tempFirstString.locate(tempSecondString);
        System.out.println("The position of \"" + tempSecondString + "\" in \"" + tempFirstString
                + "\" is: " + tempPosition);

        MyString tempThirdString = new MyString("ki");
        tempPosition = tempFirstString.locate(tempThirdString);
        System.out.println("The position of \"" + tempThirdString + "\" in \"" + tempFirstString
                + "\" is: " + tempPosition);

        tempThirdString = tempFirstString.substring(1, 2);
        System.out.println("The substring is: \"" + tempThirdString + "\"");

        tempThirdString = tempFirstString.substring(5, 5);
        System.out.println("The substring is: \"" + tempThirdString + "\"");

        tempThirdString = tempFirstString.substring(5, 6);
        System.out.println("The substring is: \"" + tempThirdString + "\"");
    }

}
