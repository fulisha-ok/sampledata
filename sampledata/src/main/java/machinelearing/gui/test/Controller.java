package machinelearing.gui.test;

/**
 * @author： fulisha
 * @date： 2023/7/23 17:14
 * @description：
 */
public class Controller {
    Flying flying;

    Controller(){
        flying = null;
    }

    void setListener(Flying paraFlying){
        flying = paraFlying;
    }

    void doIt(){
        flying.fly();
    }
}
