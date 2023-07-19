package machinelearing.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author： fulisha
 * @date： 2023/7/17 15:41
 * @description：
 */
public class DialogCloser extends WindowAdapter implements ActionListener {
    /**
     * The dialog under control.
     */
    private Dialog currentDialog;

    /**
     * The first constructor.
     */
    public DialogCloser() {
        super();
    }

    /**
     * The second constructor.
     *
     * @param paraDialog the dialog under control
     */
    public DialogCloser(Dialog paraDialog) {
        currentDialog = paraDialog;
    }

    /**
     * Close the dialog which clicking the cross at the up-right corner of the window.
     * @param paraWindowEvent  From it we can obtain which window sent the message because X was used.
     */
    @Override
    public void windowClosing(WindowEvent paraWindowEvent) {
        paraWindowEvent.getWindow().dispose();
    }

    /**
     * Close the dialog while pushing an "OK" or "Cancel" button.
     * @param paraEvent Not considered.
     */
    @Override
    public void actionPerformed(ActionEvent paraEvent) {
        currentDialog.dispose();
    }
}
