package machinelearing.gui;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * @author： fulisha
 * @date： 2023/7/17 16:10
 * @description：
 */
public class DoubleField extends TextField implements FocusListener {

    /**
     * Serial uid. Not quite useful.
     */
    private static final long serialVersionUID = 363634723L;

    /**
     * The value
     */
    protected double doubleValue;

    /**
     * Give it default values.
     */
    public DoubleField() {
        this("5.13", 10);
    }
    /**
     * Only specify the content.
     * @param paraString The content of the field.
     */
    public DoubleField(String paraString) {
        this(paraString, 10);
    }

    /**
     * Only specify the width.
     * @param paraWidth  The width of the field.
     */
    public DoubleField(int paraWidth) {
        this("5.13", paraWidth);
    }

    /**
     * Specify the content and the width.
     * @param paraString The content of the field.
     * @param paraWidth The width of the field.
     */
    public DoubleField(String paraString, int paraWidth) {
        super(paraString, paraWidth);
        addFocusListener(this);
    }

    /**
     * Implement FocusListener.
     * @param paraEvent  The event is unimportant.
     */
    @Override
    public void focusGained(FocusEvent paraEvent) {
    }

    /**
     * Implement FocusListener.
     * @param paraEvent The event is unimportant.
     */
    @Override
    public void focusLost(FocusEvent paraEvent) {
        try {
            doubleValue = Double.parseDouble(getText());
        } catch (Exception ee) {
            ErrorDialog.errorDialog
                    .setMessageAndShow("\"" + getText() + "\" Not a double. Please check.");
            requestFocus();
        }
    }

    /**
     * Get the double value.
     * @return the double value.
     */
    public double getValue() {
        try {
            doubleValue = Double.parseDouble(getText());
        } catch (Exception ee) {
            ErrorDialog.errorDialog
                    .setMessageAndShow("\"" + getText() + "\" Not a double. Please check.");
            requestFocus();
        }
        return doubleValue;
    }
}
