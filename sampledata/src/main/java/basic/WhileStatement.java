package basic;

/**
 * @author： fulisha
 * @date： 2023/4/4 14:37
 * @description： This is the ninth code. Names and comments should follow my style strictly.
 */
public class WhileStatement {
    public static void main(String[] args) {
        whileStatementTest();
    }

    /**
     * The sum not exceeding a given value.
     */
    public static void whileStatementTest() {
        int tempMax = 100;
        int tempValue = 0;
        int tempSum = 0;

        // Approach 1.
        while (tempSum <= tempMax) {
            tempValue++;
            tempSum += tempValue;
            System.out.println("tempValue = " + tempValue + ", tempSum = " + tempSum);
        }
        tempSum -= tempValue;

        System.out.println("The sum not exceeding " + tempMax + " is: " + tempSum);

        // Approach 2.
        System.out.println("\r\nAlternative approach.");
        tempValue = 0;
        tempSum = 0;
        while (true) {
            tempValue++;
            tempSum += tempValue;
            System.out.println("tempValue = " + tempValue + ", tempSum = " + tempSum);

            if (tempMax < tempSum) {
                break;
            }
        }
        tempSum -= tempValue;

        System.out.println("The sum not exceeding " + tempMax + " is: " + tempSum);
    }
}
