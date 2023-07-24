package machinelearing.gui.test;

/**
 * @author： fulisha
 * @date： 2023/7/23 17:15
 * @description：
 */
public class Bird implements Flying {
    double weight = 0.5;

    @Override
    public void fly(){
        System.out.println("Bird fly, my weight is " + weight + " kg.");
    }
}
