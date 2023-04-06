package datastructure.list;
/**
 * @Author: fulisha
 * @Date: 2023-04-05 17:34
 * @desription
 */
public class LinkedList {
    class Node{
        int data;
        Node next;

        /**
         * The constructor
         * @param paraValue The data.
         */
        public Node(int paraValue){
            data = paraValue;
            next = null;
        }
    }

    Node header;

    /**
     * Construct an empty linked list.
     */
    public LinkedList(){
        header = new Node(0);
    }

    /**
     * Overrides the method claimed in Object, the superclass of any class.
     * @return
     */
    public String toString(){
        String resultString = "";

        if (header.next == null){
            return "empty";
        }

        Node tempNode = header.next;
        while (tempNode != null){
            resultString += tempNode.data + ",";
            tempNode  = tempNode.next;
        }

        return resultString;
    }

    /**
     * Reset to empty. Free the space through garbage collection.
     */
    public void reset() {
        header.next = null;
    }

    /**
     * Locate the given value. If it appears in multiple positions, simply return the first one.
     * @param paraValue The given value.
     * @return The position. -1 for not found.
     */
    public int locate(int paraValue){
        int tempPosition = -1;

        Node tempNode = header.next;
        int tempCurrentPosition = 0;
        while (tempNode != null){
            if (tempNode.data == paraValue){
                tempPosition = tempCurrentPosition;
                break;
            }

            tempNode = tempNode.next;
            tempCurrentPosition++;
        }

        return  tempCurrentPosition;
    }

    /**
     * Insert a value to a position. If the list is already full, do nothing.
     * @param paraPosition The given position.
     * @param paraValue The given value.
     * @return Success or not.
     */
    public boolean insert(int paraPosition, int paraValue){
        Node tempNode = header;
        Node tempNewNode;

        //find a preNode
        for (int i = 0; i<paraPosition; i++){
            if (tempNode.next == null){
                System.out.println("The position " + paraPosition + " is illegal.");
                return false;
            }
            tempNode = tempNode.next;
        }

        tempNewNode = new Node(paraValue);

        tempNewNode.next = tempNode.next;
        tempNode.next = tempNewNode;

        return  true;
    }


    /**
     * Delete a value at a position.
     * @param paraPosition The given position.
     * @return Success or not
     */
    public boolean delete(int paraPosition){
        if (header.next == null){
            System.out.println("Cannot delete element from an empty list.");
            return false;
        }

        Node tempNode = header;

        for (int i = 0; i < paraPosition; i++){
            if (tempNode.next == null){
                System.out.println("The position " + paraPosition + " is illegal.");
                return false;
            }

            tempNode = tempNode.next;
        }

        tempNode.next = tempNode.next.next;
        return true;
    }

    public static void main(String args[]) {
        LinkedList tempFirstList = new LinkedList();
        System.out.println("Initialized, the list is: " + tempFirstList.toString());

        for (int i = 0; i < 5; i++) {
            tempFirstList.insert(0, i);
        }
        System.out.println("Inserted, the list is: " + tempFirstList.toString());

        tempFirstList.insert(6, 9);

        tempFirstList.delete(4);

        tempFirstList.delete(2);
        System.out.println("Deleted, the list is: " + tempFirstList.toString());

        tempFirstList.delete(0);
        System.out.println("Deleted, the list is: " + tempFirstList.toString());

        for (int i = 0; i < 5; i++) {
            tempFirstList.delete(0);
            System.out.println("Looped delete, the list is: " + tempFirstList.toString());
        }
    }
}

