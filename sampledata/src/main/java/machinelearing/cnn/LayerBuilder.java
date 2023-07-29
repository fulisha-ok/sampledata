package machinelearing.cnn;

import java.util.ArrayList;
import java.util.List;

/**
 * @author： fulisha
 * @date： 2023/7/29 16:10
 * @description：
 */
public class LayerBuilder {
    /**
     * Layers.
     */
    private List<CnnLayer> layers;

    /**
     * The first constructor.
     */
    public LayerBuilder() {
        layers = new ArrayList<CnnLayer>();
    }// Of the first constructor

    /**
     * The second constructor.
     */
    public LayerBuilder(CnnLayer paraLayer) {
        this();
        layers.add(paraLayer);
    }

    /**
     * Add a layer.
     * @param paraLayer The new layer.
     */
    public void addLayer(CnnLayer paraLayer) {
        layers.add(paraLayer);
    }

    /**
     * Get the specified layer.
     * @param paraIndex  The index of the layer.
     */
    public CnnLayer getLayer(int paraIndex) throws RuntimeException{
        if (paraIndex >= layers.size()) {
            throw new RuntimeException("CnnLayer " + paraIndex + " is out of range: "
                    + layers.size() + ".");
        }

        return layers.get(paraIndex);
    }

    /**
     * Get the output layer.
     */
    public CnnLayer getOutputLayer() {
        return layers.get(layers.size() - 1);
    }

    /**
     * Get the number of layers.
     */
    public int getNumLayers() {
        return layers.size();
    }
}
