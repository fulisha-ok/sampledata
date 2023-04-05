package datastructure.list;


/**
 * @Author: fulisha
 * @Date: 2023-04-05 16:33
 * @desription
 */
public class SequentialList {
    /**
     * The maximal length of the list. It is a constant.
     */
    public  static final int MAX_LENGTH = 10;

    /**
     * The actual length not exceeding MAX_LENGTH. Attention: length is not only the member variable of Sequential list,
     * but also the member variable of Array. In fact, a name can be the member variable of different classes.
     */
    int length;

    /**
     * The data stored in an array.
     */
    int[] data;

    /**
     *  Construct an empty sequential list.
     */
    public SequentialList(){
        length = 0;
        data = new int[MAX_LENGTH];
    }

    /**
     * Construct a sequential list using an array.
     * @param paraArray The given array. Its length should not exceed MAX_LENGTH. For simplicity now we do not check it.
     */
    public SequentialList(int[] paraArray){
        data = new int[MAX_LENGTH];
        length = paraArray.length;
        for (int i = 0; i < paraArray.length; i++) {
            data[i] = paraArray[i];
        }
    }

    /**
     * Overrides the method claimed in Object, the superclass of any class.
     * @return
     */
    public String toString(){
        String resultString = "";

        if (length == 0){
            return "empty";
        }

        for (int i = 0; i<length-1; i++){
            resultString += data[i] + ",";
        }

        resultString += data[length - 1];

        return resultString;
    }

    /**
     * Reset to empty.
     */
    public void reset(){
        length = 0;
    }

    /**
     * Find the index of the given value. If it appears in multiple positions,simply return the first one.
     * @param paraValue The given value.
     * @return The position. -1 for not found.
     */
    public int indexOf(int paraValue){
        int tempPosition = -1;

        for (int i = 0; i<paraValue; i++){
            if (data[i] == paraValue){
                tempPosition = i;
                break;
            }
        }

        return tempPosition;
    }

    /**
     * Insert a value to a position. If the list is already full, do nothing.
     * @param paraPosition The given position.
     * @param paraValue The given value.
     * @return Success or not.
     */
    public boolean insert(int paraPosition, int paraValue){
        if (length == MAX_LENGTH){
            System.out.println("List full.");
            return false;
        }

        if (paraPosition < 0 || paraPosition > length){
            System.out.println("The position " + paraPosition + " is out of bounds.");
            return false;
        }

        for (int i = length; i > paraPosition; i--){
            data[i] = data[i-1];
        }

        data[paraPosition] = paraValue;
        length++;

        return true;
    }

    /**
     * Delete a value at a position.
     * @param paraPosition paraPosition The given position.
     * @return Success or not.
     */
    public boolean delete(int paraPosition){
        if (paraPosition < 0 || paraPosition >= length){
            System.out.println("The position " + paraPosition + " is out of bounds.");
            return false;
        }

        for (int i = paraPosition; i < length-1; i++){
            data[i] = data[i+1];
        }

        length--;
        return true;
    }




    public static void main(String args[]) {
        /*int[] tempArray = { 1, 4, 6, 9 };
        SequentialList tempFirstList = new SequentialList(tempArray);
        System.out.println("Initialized, the list is: " + tempFirstList.toString());
        System.out.println("Again, the list is: " + tempFirstList);

        tempFirstList.reset();
        System.out.println("After reset, the list is: " + tempFirstList);*/

        int[] tempArray = { 1, 4, 6, 9 };
        SequentialList tempFirstList = new SequentialList(tempArray);
        System.out.println("After initialization, the list is: " + tempFirstList.toString());
        System.out.println("Again, the list is: " + tempFirstList);

        int tempValue = 4;
        int tempPosition = tempFirstList.indexOf(tempValue);
        System.out.println("The position of " + tempValue + " is " + tempPosition);

        tempValue = 5;
        tempPosition = tempFirstList.indexOf(tempValue);
        System.out.println("The position of " + tempValue + " is " + tempPosition);

        tempPosition = 2;
        tempValue = 5;
        tempFirstList.insert(tempPosition, tempValue);
        System.out.println(
                "After inserting " + tempValue + " to position " + tempPosition + ", the list is: " + tempFirstList);

        tempPosition = 8;
        tempValue = 10;
        tempFirstList.insert(tempPosition, tempValue);
        System.out.println(
                "After inserting " + tempValue + " to position " + tempPosition + ", the list is: " + tempFirstList);

        tempPosition = 3;
        tempFirstList.delete(tempPosition);
        System.out.println("After deleting data at position " + tempPosition + ", the list is: " + tempFirstList);

        for (int i = 0; i < 8; i++) {
            tempFirstList.insert(i, i);
            System.out.println("After inserting " + i + " to position " + i + ", the list is: " + tempFirstList);
        } // Of for i

        tempFirstList.reset();
        System.out.println("After reset, the list is: " + tempFirstList);
    }


}
