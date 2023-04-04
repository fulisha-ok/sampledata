package basic;

/**
 * @author： fulisha
 * @date： 2023/4/4 13:59
 * @description： This is the sixth code. Names and comments should follow my style strictly.
 */
public class ForStatement {
    /**
     * The entrance of the program.
     * @param args
     */
    public static void main(String[] args) {
        forStatementTest();
    }

    /**
     * Method unit test.
     */
    public static void forStatementTest(){
        int tempN = 0;
        System.out.println("1 add to " + tempN + " is: " + addToN(tempN));

        tempN = 0;
        System.out.println("1 add to " + tempN + " is: " + addToN(tempN));

        int tempStepLength = 1;
        tempN = 10;
        System.out.println("1 add to " + tempN + " with step length " + tempStepLength + " is: "
                + addToNWithStepLength(tempN, tempStepLength));

        tempStepLength = 2;
        System.out.println("1 add to " + tempN + " with step length " + tempStepLength + " is: "
                + addToNWithStepLength(tempN, tempStepLength));
    }

    /**
     * Add from 1 to N.
     * @param paraN The given upper bound.
     * @return The sum.
     */
    public static int addToN(int paraN) {
        int resultSum = 0;

        for (int i = 1; i <= paraN; i++) {
            resultSum += i;
        }

        return resultSum;
    }

    /**
     * Add from 1 to N with a step length.
     * @param paraN The given upper bound.
     * @param paraStepLength paraStepLength The given step length.
     * @return The sum.
     */
    public static int addToNWithStepLength(int paraN, int paraStepLength) {
        int resultSum = 0;

        for (int i = 1; i <= paraN; i += paraStepLength) {
            resultSum += i;
        }

        return resultSum;
    }


}
