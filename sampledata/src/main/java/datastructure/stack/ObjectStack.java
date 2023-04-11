package datastructure.stack;

import sun.applet.Main;

/**
 * @Author: fulisha
 * @Date: 2023-04-11 21:35
 * @desription
 */
public class ObjectStack {
    /**
     * The depth.
     */
    public static final int MAX_DEPTH = 10;

    /**
     * The actual depth.
     */
    int depth;

    /**
     * The data
     */
    Object[] data;

    /**
     * Construct an empty sequential list.
     */
    public ObjectStack() {
        depth = 0;
        data = new Object[MAX_DEPTH];
    }

    /**
     * Overrides the method claimed in Object, the superclass of any class.
     * @return
     */
    public String toString() {
        String resultString = "";
        for (int i = 0; i < depth; i++) {
            resultString += data[i];
        }

        return resultString;
    }

    /**
     * Push an element.
     * @param paraObject  The given object.
     * @return Success or not.
     */
    public boolean push(Object paraObject){
        if (depth == MAX_DEPTH) {
            System.out.println("Stack full.");
            return false;
        }

        data[depth] = paraObject;
        depth++;

        return true;
    }

    /**
     * Pop an element.
     * @return The object at the top of the stack.
     */
    public Object pop() {
        if (depth == 0) {
            System.out.println("Nothing to pop.");
            return '\0';
        }

        Object resultObject = data[depth - 1];
        depth--;

        return resultObject;
    }

    /**
     * Is the stack empty?
     * @return True if empty.
     */
    public boolean isEmpty() {
        if (depth == 0) {
            return true;
        }

        return false;
    }

    public static void main(String args[]) {
        ObjectStack tempStack = new ObjectStack();

        for (char ch = 'a'; ch < 'm'; ch++) {
            tempStack.push(new Character(ch));
            System.out.println("The current stack is: " + tempStack);
        }

        char tempChar;
        for (int i = 0; i < 12; i++) {
            tempChar = ((Character)tempStack.pop()).charValue();
            System.out.println("Poped: " + tempChar);
            System.out.println("The current stack is: " + tempStack);
        }
    }

}
