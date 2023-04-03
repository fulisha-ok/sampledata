package basic;

/**
 * @author： fulisha
 * @date： 2023/4/3 17:54
 * @description：
 */
public class SwitchStatement {

    public static void main(String args[]) {
        scoreToLevelTest();
    }// Of main

    /**
     * Score to level.
     * @param paraScore
     * @return
     */
    public static char scoreToLevel(int paraScore) {

        char resultLevel = 'E';

        int tempDigitalLevel = paraScore / 10;

        // The use of break is important.
        switch (tempDigitalLevel) {
            case 10:
            case 9:
                resultLevel = 'A';
                break;
            case 8:
                resultLevel = 'B';
                break;
            case 7:
                resultLevel = 'C';
                break;
            case 6:
                resultLevel = 'D';
                break;
            case 5:
            case 4:
            case 3:
            case 2:
            case 1:
            case 0:
                resultLevel = 'F';
                break;
            default:
                resultLevel = 'E';
        }

        return resultLevel;
    }

    /**
     * Method unit test.
     */
    public static void scoreToLevelTest() {
        int tempScore = 100;
        System.out.println("Score " + tempScore + " to level is: " + scoreToLevel(tempScore));

        tempScore = 91;
        System.out.println("Score " + tempScore + " to level is: " + scoreToLevel(tempScore));

        tempScore = 82;
        System.out.println("Score " + tempScore + " to level is: " + scoreToLevel(tempScore));

        tempScore = 75;
        System.out.println("Score " + tempScore + " to level is: " + scoreToLevel(tempScore));

        tempScore = 66;
        System.out.println("Score " + tempScore + " to level is: " + scoreToLevel(tempScore));

        tempScore = 52;
        System.out.println("Score " + tempScore + " to level is: " + scoreToLevel(tempScore));

        tempScore = 8;
        System.out.println("Score " + tempScore + " to level is: " + scoreToLevel(tempScore));

        tempScore = 120;
        System.out.println("Score " + tempScore + " to level is: " + scoreToLevel(tempScore));
    }

}
