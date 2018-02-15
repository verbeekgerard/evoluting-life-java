package eu.luminis.brains;

import org.apache.commons.math3.linear.*;

class Brain implements IBrain {
    private final Layer outputLayer;
    private final Layer[] hiddenLayers;
    private final InputLayer inputLayer;

    Brain(Layer outputLayer, Layer[] hiddenLayers, InputLayer inputLayer) {
        this.outputLayer = outputLayer;
        this.hiddenLayers = hiddenLayers;
        this.inputLayer = inputLayer;
    }

    @Override
    public double[] think(double[] input) {
        this.inputLayer.sense(input).transmit();

        for (int i = hiddenLayers.length-1; i >= 0; i--) {
            Layer layer = hiddenLayers[i];
            layer.transmit();
        }

        return outputLayer.transmit();
    }
}