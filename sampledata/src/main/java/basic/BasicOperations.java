package basic;

/**
 * @author： fulisha
 * @date： 2023/4/3 17:36
 * @description：
 */
public class BasicOperations {

    public static void main(String args[]) {
        int tempFirstInt, tempSecondInt, tempResultInt;
        double tempFirstDouble, tempSecondDouble, tempResultDouble;

        tempFirstInt = 15;
        tempSecondInt = 4;

        tempFirstDouble = 1.2;
        tempSecondDouble = 3.5;

        //Addition
        tempResultInt = tempFirstInt + tempSecondInt;
        tempResultDouble = tempFirstDouble + tempSecondDouble;

        System.out.println("" + tempFirstInt + " + " + tempSecondInt + " = " + tempResultInt);
        System.out.println("" + tempFirstDouble + " + " + tempSecondDouble + " = " + tempResultDouble);

        //Subtraction
        tempResultInt = tempFirstInt - tempSecondInt;
        tempResultDouble = tempFirstDouble - tempSecondDouble;

        System.out.println("" + tempFirstInt + " - " + tempSecondInt + " = " + tempResultInt);
        System.out.println("" + tempFirstDouble + " - " + tempSecondDouble + " = " + tempResultDouble);

        //Multiplication
        tempResultInt = tempFirstInt * tempSecondInt;
        tempResultDouble = tempFirstDouble * tempSecondDouble;

        System.out.println("" + tempFirstInt + " * " + tempSecondInt + " = " + tempResultInt);
        System.out.println("" + tempFirstDouble + " * " + tempSecondDouble + " = " + tempResultDouble);

        //Division
        tempResultInt = tempFirstInt / tempSecondInt;
        tempResultDouble = tempFirstDouble / tempSecondDouble;

        System.out.println("" + tempFirstInt + " / " + tempSecondInt + " = " + tempResultInt);
        System.out.println("" + tempFirstDouble + " / " + tempSecondDouble + " = " + tempResultDouble);

        //Modulus
        tempResultInt = tempFirstInt % tempSecondInt;

        System.out.println("" + tempFirstInt + " % " + tempSecondInt + " = " + tempResultInt);
    }
}

