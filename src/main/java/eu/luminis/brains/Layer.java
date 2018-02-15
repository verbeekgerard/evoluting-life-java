package eu.luminis.brains;

import org.apache.commons.math3.analysis.function.*;
import org.apache.commons.math3.linear.*;

class Layer {
    private final RealMatrix weights;
    private final RealVector biases;

    public Layer(RealMatrix weights, RealVector biases) {
        this.weights = weights;
        this.biases = biases;
    }

    public RealVector transmit(RealVector input) {
        return weights.operate(input).add(biases).map(new Tanh());
    }
}
