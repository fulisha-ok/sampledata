package machinelearing.gui;

import javax.swing.*;
import java.awt.*;

/**
 * @author： fulisha
 * @date： 2023/7/17 15:43
 * @description：
 */
public class GUICommon {
    /**
     * Only one main frame.
     */
    public static Frame mainFrame = null;

    /**
     * Only one main pane.
     */
    public static JTabbedPane mainPane = null;

    /**
     * For default project number.
     */
    public static int currentProjectNumber = 0;

    /**
     * Default font.
     */
    public static final Font MY_FONT = new Font("Times New Romans", Font.PLAIN, 12);

    /**
     * Default color
     */
    public static final Color MY_COLOR = Color.lightGray;

    /**
     * Set the main frame. This can be done only once at the initialzing stage.
     * @param paraFrame the main frame of the GUI.
     * @throws Exception If the main frame is set more than once.
     */
    public static void setFrame(Frame paraFrame) throws Exception {
        if (mainFrame == null) {
            mainFrame = paraFrame;
        } else {
            throw new Exception("The main frame can be set only ONCE!");
        }
    }

    /**
     * Set the main pane. This can be done only once at the initialzing stage.
     * @param paramPane the main pane of the GUI.
     * @throws Exception  If the main panel is set more than once.
     */
    public static void setPane(JTabbedPane paramPane) throws Exception {
        if (mainPane == null) {
            mainPane = paramPane;
        } else {
            throw new Exception("The main panel can be set only ONCE!");
        }
    }
}
