package machinelearing.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

/**
 * @author： fulisha
 * @date： 2023/7/17 16:14
 * @description：一个带有文件选择功能的文本输入框，用于方便用户选择文件路径，并将选择的文件路径显示在文本输入框中。它还具有一些对文本内容的处理，如检查文件是否存在并显示错误消息等。
 */
public class FilenameField extends TextField implements ActionListener,
        FocusListener{
    /**
     * Serial uid. Not quite useful.
     */
    private static final long serialVersionUID = 4572287941606065298L;

    /**
     * No special initialization..
     */
    public FilenameField() {
        super();
        setText("");
        addFocusListener((FocusListener) this);
    }

    /**
     * No special initialization.
     * @param paraWidth The width of the .
     */
    public FilenameField(int paraWidth) {
        super(paraWidth);
        setText("");
        addFocusListener(this);
    }// Of constructor

    /**
     * No special initialization.
     * @param paraWidth The width of the .
     * @param paraText  The given initial text
     */
    public FilenameField(int paraWidth, String paraText) {
        super(paraWidth);
        setText(paraText);
        addFocusListener(this);
    }

    /**
     * No special initialization.
     * @param paraWidth The width of the .
     * @param paraText The given initial text
     */
    public FilenameField(String paraText, int paraWidth) {
        super(paraWidth);
        setText(paraText);
        addFocusListener(this);
    }

    /**
     * Avoid setting null or empty string.
     * @param paraText The given text.
     */
    @Override
    public void setText(String paraText) {
        if (paraText.trim().equals("")) {
            super.setText("unspecified");
        } else {
            super.setText(paraText.replace('\\', '/'));
        }
    }

    /**
     * Implement ActionListenter.
     * @param paraEvent The event is unimportant.
     */
    @Override
    public void actionPerformed(ActionEvent paraEvent) {
        FileDialog tempDialog = new FileDialog(GUICommon.mainFrame,
                "Select a file");
        tempDialog.setVisible(true);
        if (tempDialog.getDirectory() == null) {
            setText("");
            return;
        }

        String directoryName = tempDialog.getDirectory();

        String tempFilename = directoryName + tempDialog.getFile();
        //System.out.println("tempFilename = " + tempFilename);

        setText(tempFilename);
    }

    /**
     * Implement FocusListenter.
     * @param paraEvent  The event is unimportant.
     */
    @Override
    public void focusGained(FocusEvent paraEvent) {
    }

    /**
     * Implement FocusListenter.
     * @param paraEvent The event is unimportant.
     */
    @Override
    public void focusLost(FocusEvent paraEvent) {
        // System.out.println("Focus lost exists.");
        String tempString = getText();
        if ((tempString.equals("unspecified"))
                || (tempString.equals("")))
            return;
        File tempFile = new File(tempString);
        if (!tempFile.exists()) {
            ErrorDialog.errorDialog.setMessageAndShow("File \"" + tempString
                    + "\" not exists. Please check.");
            requestFocus();
            setText("");
        }
    }
}
