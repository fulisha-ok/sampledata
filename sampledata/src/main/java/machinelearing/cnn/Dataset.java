package machinelearing.cnn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author： fulisha
 * @date： 2023/7/29 13:47
 * @description：
 */
public class Dataset {

    /**
     * All instances organized by a list.
     */
    private List<Instance> instances;

    /**
     * The label index.
     */
    private int labelIndex;

    /**
     * The max label (label start from 0).
     */
    private double maxLabel = -1;

    /**
     * The first constructor.
     */
    public Dataset() {
        labelIndex = -1;
        instances = new ArrayList<Instance>();
    }

    /**
     * The second constructor.
     * @param paraFilename The filename.
     * @param paraSplitSign Often comma.
     * @param paraLabelIndex Often the last column.
     */
    public Dataset(String paraFilename, String paraSplitSign, int paraLabelIndex) {
        instances = new ArrayList<Instance>();
        labelIndex = paraLabelIndex;

        File tempFile = new File(paraFilename);
        try {
            BufferedReader tempReader = new BufferedReader(new FileReader(tempFile));
            String tempLine;
            while ((tempLine = tempReader.readLine()) != null) {
                String[] tempDatum = tempLine.split(paraSplitSign);
                if (tempDatum.length == 0) {
                    continue;
                } // Of if

                double[] tempData = new double[tempDatum.length];
                for (int i = 0; i < tempDatum.length; i++) {
                    tempData[i] = Double.parseDouble(tempDatum[i]);
                }
                Instance tempInstance = new Instance(tempData);
                append(tempInstance);
            } // Of while
            tempReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to load " + paraFilename);
            System.exit(0);
        }//Of try
    }// Of the second constructor

    /**
     * Append an instance.
     * @param paraInstance  The given record.
     */
    public void append(Instance paraInstance) {
        instances.add(paraInstance);
    }

    /**
     * Append an instance  specified by double values.
     */
    public void append(double[] paraAttributes, Double paraLabel) {
        instances.add(new Instance(paraAttributes, paraLabel));
    }

    /**
     * Getter.
     */
    public Instance getInstance(int paraIndex) {
        return instances.get(paraIndex);
    }

    /**
     * Getter.
     */
    public int size() {
        return instances.size();
    }

    /**
     * Getter.
     */
    public double[] getAttributes(int paraIndex) {
        return instances.get(paraIndex).getAttributes();
    }

    /**
     * Getter.
     */
    public Double getLabel(int paraIndex) {
        return instances.get(paraIndex).getLabel();
    }

    /**
     * Unit test.
     */
    public static void main(String args[]) {
        Dataset tempData = new Dataset("D:/sampledata/sampledata/src/data/train.format", ",", 784);
        Instance tempInstance = tempData.getInstance(0);
        System.out.println("The first instance is: " + tempInstance);
    }

    /**
     * An instance.
     */
    public class Instance {
        /**
         * Conditional attributes.
         */
        private double[] attributes;

        /**
         * Label.
         */
        private Double label;

        /**
         * The first constructor.
         */
        private Instance(double[] paraAttrs, Double paraLabel) {
            attributes = paraAttrs;
            label = paraLabel;
        }

        /**
         * The second constructor.
         */
        public Instance(double[] paraData) {
            if (labelIndex == -1)
            {
                // No label
                attributes = paraData;
            } else {
                label = paraData[labelIndex];
                if (label > maxLabel) {
                    // It is a new label
                    maxLabel = label;
                }

                if (labelIndex == 0) {
                    // The first column is the label
                    attributes = Arrays.copyOfRange(paraData, 1, paraData.length);
                } else {
                    // The last column is the label
                    attributes = Arrays.copyOfRange(paraData, 0, paraData.length - 1);
                }
            }
        }

        public double[] getAttributes() {
            return attributes;
        }

        public Double getLabel() {
            if (labelIndex == -1) {
                return null;
            }
            return label;
        }

        @Override
        public String toString(){
            return Arrays.toString(attributes) + ", " + label;
        }
    }
}

