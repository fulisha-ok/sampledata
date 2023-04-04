package basic;

/**
 * @author： fulisha
 * @date： 2023/4/3 17:52
 * @description：
 */
public class LeapYear {

    /**
     * The entrance of the program.
     * @param args
     */
    public static void main(String args[]) {
        // Test isLeapYear
        int tempYear = 2021;

        System.out.print("" + tempYear + " is ");
        if (!isLeapYear(tempYear)) {
            System.out.print("NOT ");
        } // Of if
        System.out.println("a leap year.");

        tempYear = 2000;

        System.out.print("" + tempYear + " is ");
        if (!isLeapYear(tempYear)) {
            System.out.print("NOT ");
        }
        System.out.println("a leap year.");

        tempYear = 2100;

        System.out.print("" + tempYear + " is ");
        if (!isLeapYear(tempYear)) {
            System.out.print("NOT ");
        }
        System.out.println("a leap year.");

        tempYear = 2004;

        System.out.print("" + tempYear + " is ");
        if (!isLeapYear(tempYear)) {
            System.out.print("NOT ");
        }
        System.out.println("a leap year.");

        // Test isLeapYearV2
        System.out.println("Now use the second version.");
        tempYear = 2021;

        System.out.print("" + tempYear + " is ");
        if (!isLeapYearV2(tempYear)) {
            System.out.print("NOT ");
        }
        System.out.println("a leap year.");

        tempYear = 2000;

        System.out.print("" + tempYear + " is ");
        if (!isLeapYearV2(tempYear)) {
            System.out.print("NOT ");
        }
        System.out.println("a leap year.");

        tempYear = 2100;

        System.out.print("" + tempYear + " is ");
        if (!isLeapYearV2(tempYear)) {
            System.out.print("NOT ");
        }
        System.out.println("a leap year.");

        tempYear = 2004;

        System.out.print("" + tempYear + " is ");
        if (!isLeapYearV2(tempYear)) {
            System.out.print("NOT ");
        }
        System.out.println("a leap year.");
    }

    /**
     * @param paraYear
     * @return Is the given year leap? true or false;
     */
    public static boolean isLeapYear(int paraYear) {
        if ((paraYear % 4 == 0) && (paraYear % 100 != 0) || (paraYear % 400 == 0)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param paraYear
     * @return Is the given year leap? Replace the complex condition with a number of if. return true or false
     */
    public static boolean isLeapYearV2(int paraYear) {
        if (paraYear % 4 != 0) {
            return false;
        } else if (paraYear % 400 == 0) {
            return true;
        } else if (paraYear % 100 == 0) {
            return false;
        } else {
            return true;
        }
    }

}
