package machinelearing.gui;

import java.awt.*;

/**
 * @author： fulisha
 * @date： 2023/7/17 16:04
 * @description：
 */
public class ErrorDialog  extends Dialog{
    /**
     * Serial uid. Not quite useful.
     */
    private static final long serialVersionUID = 124535235L;

    /**
     * The ONLY ErrorDialog.
     */
    public static ErrorDialog errorDialog = new ErrorDialog();

    /**
     * The label containing the message to display.
     */
    private TextArea messageTextArea;

    /**
     ***************************
     * Display an error dialog and respective error message. Like other dialogs,
     * this constructor is private, such that users can use only one dialog,
     * i.e., ErrorDialog.errorDialog to display message. This is helpful for
     * saving space (only one dialog) since we may need "many" dialogs.
     ***************************
     */
    private ErrorDialog(){
        // This dialog is module.
        super(GUICommon.mainFrame, "Error", true);

        // Prepare for the dialog.
        messageTextArea = new TextArea();

        Button okButton = new Button("OK");
        okButton.setSize(20, 10);
        okButton.addActionListener(new DialogCloser(this));
        Panel okPanel = new Panel();
        okPanel.setLayout(new FlowLayout());
        okPanel.add(okButton);

        // Add TextArea and Button
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, messageTextArea);
        add(BorderLayout.SOUTH, okPanel);

        setLocation(200, 200);
        setSize(500, 200);
        addWindowListener(new DialogCloser());
        setVisible(false);
    }

    /**
     * set message.
     * @param paramMessage the new message
     */
    public void setMessageAndShow(String paramMessage) {
        messageTextArea.setText(paramMessage);
        setVisible(true);
    }
}
