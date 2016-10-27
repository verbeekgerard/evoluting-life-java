package eu.luminis.brains;

import java.util.List;

class Brain implements IBrain {
    private final Layer outputLayer;
    private final List<Layer> layers;
    private final InputLayer inputLayer;

    Brain(Layer outputLayer, List<Layer> layers, InputLayer inputLayer) {
        this.outputLayer = outputLayer;
        this.layers = layers;
        this.inputLayer = inputLayer;
    }

    @Override
    public List<Double> think(List<Double> input) {
        this.inputLayer.sense(input).transmit();

        for (int i = layers.size()-1; i >= 0; i--) {
            Layer layer = layers.get(i);
            layer.transmit();
        }

        return outputLayer.transmit();
    }
}