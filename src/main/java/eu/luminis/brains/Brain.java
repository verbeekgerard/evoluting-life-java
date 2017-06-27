package eu.luminis.brains;

import java.util.List;

class Brain implements IBrain {
    private final Layer outputLayer;
    private final List<Layer> hiddenLayers;
    private final InputLayer inputLayer;

    Brain(Layer outputLayer, List<Layer> hiddenLayers, InputLayer inputLayer) {
        this.outputLayer = outputLayer;
        this.hiddenLayers = hiddenLayers;
        this.inputLayer = inputLayer;
    }

    @Override
    public double[] think(double[] input) {
        this.inputLayer.sense(input).transmit();

        for (int i = hiddenLayers.size()-1; i >= 0; i--) {
            Layer layer = hiddenLayers.get(i);
            layer.transmit();
        }

        return outputLayer.transmit();
    }
}