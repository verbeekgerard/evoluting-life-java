package eu.luminis.brains;

import org.apache.commons.math3.linear.*;

class NeuralNetwork implements IBrain {
    private final Layer[] layers;

    NeuralNetwork(Layer[] layers) {
        this.layers = layers;
    }

    @Override
    public double[] think(double[] input) {
        RealVector activationVector = new ArrayRealVector(input);

        for (int i = 0; i<layers.length; i++) {
            Layer layer = layers[i];
            activationVector = layer.transmit(activationVector);
        }

        return activationVector.toArray();
    }
}
