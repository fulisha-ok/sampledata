package machinelearing.gui.test;

/**
 * @author： fulisha
 * @date： 2023/7/23 17:17
 * @description：
 */
public class InterfaceTest {
    public static void main(String[] args){
        Controller tempController = new Controller();
        Flying tempFlying1 = new Bird();
        tempController.setListener(tempFlying1);
        tempController.doIt();

        Flying tempFlying2 = new Plane();
        tempController.setListener(tempFlying2);
        tempController.doIt();
    }
}
