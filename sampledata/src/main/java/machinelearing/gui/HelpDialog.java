package machinelearing.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author： fulisha
 * @date： 2023/7/17 15:44
 * @description：
 */
public class HelpDialog extends Dialog implements ActionListener {
    /**
     * Serial uid. Not quite useful.
     */
    private static final long serialVersionUID = 3869415040299264995L;

    /**
     * Display the help dialog.
     *
     * @param paraTitle the title of the dialog.
     * @param paraFilename   the help file.
     */
    public HelpDialog(String paraTitle, String paraFilename) {
        super(GUICommon.mainFrame, paraTitle, true);
        setBackground(GUICommon.MY_COLOR);

        TextArea displayArea = new TextArea("", 10, 10, TextArea.SCROLLBARS_VERTICAL_ONLY);
        displayArea.setEditable(false);
        String textToDisplay = "";
        try {
            RandomAccessFile helpFile = new RandomAccessFile(paraFilename, "r");
            String tempLine = helpFile.readLine();
            while (tempLine != null) {
                textToDisplay = textToDisplay + tempLine + "\n";
                tempLine = helpFile.readLine();
            }
            helpFile.close();
        } catch (IOException ee) {
            dispose();
            ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
        }
        // Use this if you need to display Chinese. Consult the author for this
        // method.
        // textToDisplay = SimpleTools.GB2312ToUNICODE(textToDisplay);
        displayArea.setText(textToDisplay);
        displayArea.setFont(new Font("Times New Romans", Font.PLAIN, 14));

        Button okButton = new Button("OK");
        okButton.setSize(20, 10);
        okButton.addActionListener(new DialogCloser(this));
        Panel okPanel = new Panel();
        okPanel.setLayout(new FlowLayout());
        okPanel.add(okButton);

        // OK Button
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, displayArea);
        add(BorderLayout.SOUTH, okPanel);

        setLocation(120, 70);
        setSize(500, 400);
        addWindowListener(new DialogCloser());
        setVisible(false);
    }

    /**
     * Simply set it visible.
     */
    @Override
    public void actionPerformed(ActionEvent ee) {
        setVisible(true);
    }
}
