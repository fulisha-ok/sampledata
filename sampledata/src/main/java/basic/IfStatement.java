package basic;

/**
 * @author： fulisha
 * @date： 2023/4/3 17:51
 * @description：
 */
public class IfStatement {
    /**
     * The entrance of the program
     * @param args
     */
    public static void main(String args[]) {
        int tempNumber1, tempNumber2;

        // Try a positive value
        tempNumber1 = 5;

        if (tempNumber1 >= 0) {
            tempNumber2 = tempNumber1;
        } else {
            tempNumber2 = -tempNumber1;
        } // Of if

        System.out.println("The absolute value of " + tempNumber1 + " is " + tempNumber2);

        // Try a negative value
        // Lines 27 through 33 are the same as Lines 15 through 19
        tempNumber1 = -3;

        if (tempNumber1 >= 0) {
            tempNumber2 = tempNumber1;
        } else {
            tempNumber2 = -tempNumber1;
        } // Of if

        System.out.println("The absolute value of " + tempNumber1 + " is " + tempNumber2);

        // Now we use a method/function for this purpose.
        tempNumber1 = 6;
        System.out.println("The absolute value of " + tempNumber1 + " is " + abs(tempNumber1));
        tempNumber1 = -8;
        System.out.println("The absolute value of " + tempNumber1 + " is " + abs(tempNumber1));
    }

    /**
     * The absolute value of the given parameter.
     * @param paraValue The given value.
     * @return
     */
    public static int abs(int paraValue) {
        if (paraValue >= 0) {
            return paraValue;
        } else {
            return -paraValue;
        }
    }
}
