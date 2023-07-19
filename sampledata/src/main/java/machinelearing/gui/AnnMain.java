package machinelearing.gui;

import machinelearing.ann.FullAnn;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * @author： fulisha
 * @date： 2023/7/18 16:02
 * @description：
 */
public class AnnMain implements ActionListener {
    /**
     * Select the arff file.
     */
    private FilenameField arffFilenameField;

    /**
     * The setting of alpha.
     */
    private DoubleField alphaField;

    /**
     * The setting of alpha.
     */
    private DoubleField betaField;

    /**
     * The setting of alpha.
     */
    private DoubleField gammaField;

    /**
     * Layer nodes, such as "4, 8, 8, 3".
     */
    private TextField layerNodesField;

    /**
     * Activators, such as "ssa".
     */
    private TextField activatorField;

    /**
     * The number of training rounds.
     */
    private IntegerField roundsField;

    /**
     * The learning rate.
     */
    private DoubleField learningRateField;

    /**
     * The mobp.
     */
    private DoubleField mobpField;

    /**
     * The message area.
     */
    private TextArea messageTextArea;

    /**
     * The only constructor.
     */
    public AnnMain() {
        // A simple frame to contain dialogs.
        Frame mainFrame = new Frame();
        mainFrame.setTitle("ANN");
        // The top part: select arff file.
        arffFilenameField = new FilenameField(30);
        arffFilenameField.setText("D:/sampledata/sampledata/src/data/iris.arff");
        Button browseButton = new Button(" Browse ");
        browseButton.addActionListener(arffFilenameField);

        Panel sourceFilePanel = new Panel();
        sourceFilePanel.add(new Label("The .arff file:"));
        sourceFilePanel.add(arffFilenameField);
        sourceFilePanel.add(browseButton);

        // Setting panel.
        Panel settingPanel = new Panel();
        settingPanel.setLayout(new GridLayout(3, 6));

        settingPanel.add(new Label("alpha"));
        alphaField = new DoubleField("0.01");
        settingPanel.add(alphaField);

        settingPanel.add(new Label("beta"));
        betaField = new DoubleField("0.02");
        settingPanel.add(betaField);

        settingPanel.add(new Label("gamma"));
        gammaField = new DoubleField("0.03");
        settingPanel.add(gammaField);

        settingPanel.add(new Label("layer nodes"));
        layerNodesField = new TextField("4, 8, 8, 3");
        settingPanel.add(layerNodesField);

        settingPanel.add(new Label("activators"));
        activatorField = new TextField("sss");
        settingPanel.add(activatorField);

        settingPanel.add(new Label("training rounds"));
        roundsField = new IntegerField("5000");
        settingPanel.add(roundsField);

        settingPanel.add(new Label("learning rate"));
        learningRateField = new DoubleField("0.01");
        settingPanel.add(learningRateField);

        settingPanel.add(new Label("mobp"));
        mobpField = new DoubleField("0.5");
        settingPanel.add(mobpField);

        Panel topPanel = new Panel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(BorderLayout.NORTH, sourceFilePanel);
        topPanel.add(BorderLayout.CENTER, settingPanel);

        messageTextArea = new TextArea(80, 40);

        // The bottom part: ok and exit
        Button okButton = new Button(" OK ");
        okButton.addActionListener(this);
        // DialogCloser dialogCloser = new DialogCloser(this);
        Button exitButton = new Button(" Exit ");
        // cancelButton.addActionListener(dialogCloser);
        exitButton.addActionListener(ApplicationShutdown.applicationShutdown);
        Button helpButton = new Button(" Help ");
        helpButton.setSize(20, 10);
        helpButton.addActionListener(new HelpDialog("ANN", "D:/sampledata/sampledata/src/data/help.txt"));
        Panel okPanel = new Panel();
        okPanel.add(okButton);
        okPanel.add(exitButton);
        okPanel.add(helpButton);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(BorderLayout.NORTH, topPanel);
        mainFrame.add(BorderLayout.CENTER, messageTextArea);
        mainFrame.add(BorderLayout.SOUTH, okPanel);

        mainFrame.setSize(600, 500);
        mainFrame.setLocation(100, 100);
        mainFrame.addWindowListener(ApplicationShutdown.applicationShutdown);
        mainFrame.setBackground(GUICommon.MY_COLOR);
        mainFrame.setVisible(true);
    }

    /**
     * Read the arff file.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        String tempFilename = arffFilenameField.getText();

        // Read the layers nodes.
        String tempString = layerNodesField.getText().trim();

        int[] tempLayerNodes = null;
        try {
            tempLayerNodes = stringToIntArray(tempString);
        } catch (Exception ee) {
            ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
            return;
        }

        double tempLearningRate = learningRateField.getValue();
        double tempMobp = mobpField.getValue();
        String tempActivators = activatorField.getText().trim();
        FullAnn tempNetwork = new FullAnn(tempFilename, tempLayerNodes, tempLearningRate, tempMobp,
                tempActivators);
        int tempRounds = roundsField.getValue();

        long tempStartTime = new Date().getTime();
        for (int i = 0; i < tempRounds; i++) {
            tempNetwork.train();
        }
        long tempEndTime = new Date().getTime();
        messageTextArea.append("\r\nSummary:\r\n");
        messageTextArea.append("Trainng time: " + (tempEndTime - tempStartTime) + "ms.\r\n");

        double tempAccuray = tempNetwork.test();
        messageTextArea.append("Accuracy: " + tempAccuray + "\r\n");
        messageTextArea.append("End.");
    }

    /**
     * Convert a string with commas into an int array.
     * @param paraString The source string
     * @return An int array.
     * @throws Exception Exception for illegal data.
     */
    public static int[] stringToIntArray(String paraString) throws Exception {
        int tempCounter = 1;
        for (int i = 0; i < paraString.length(); i++) {
            if (paraString.charAt(i) == ',') {
                tempCounter++;
            }
        }

        int[] resultArray = new int[tempCounter];

        String tempRemainingString = new String(paraString) + ",";
        String tempString;
        for (int i = 0; i < tempCounter; i++) {
            tempString = tempRemainingString.substring(0, tempRemainingString.indexOf(",")).trim();
            if (tempString.equals("")) {
                throw new Exception("Blank is unsupported");
            }

            resultArray[i] = Integer.parseInt(tempString);

            tempRemainingString = tempRemainingString
                    .substring(tempRemainingString.indexOf(",") + 1);
        }

        return resultArray;
    }

    /**
     * The entrance method.
     * @param args The parameters.
     */
    public static void main(String args[]) {
        new AnnMain();
    }
}
