package machinelearing.gui.test;

import machinelearing.gui.test.Flying;

/**
 * @author： fulisha
 * @date： 2023/7/23 17:15
 * @description：
 */
public class Plane implements Flying {
    double price = 100000000;


    @Override
    public void fly() {
        System.out.println("Plan fly, my price is " + price + " RMB.");
    }
}
