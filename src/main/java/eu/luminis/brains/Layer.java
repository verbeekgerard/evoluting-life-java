package eu.luminis.brains;

import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.analysis.function.*;
import org.apache.commons.math3.linear.*;

class Layer {
    private final RealMatrix weights;
    private final RealVector biases;
    private final UnivariateFunction activation;

    public Layer(RealMatrix weights, RealVector biases) {
        this(weights, biases, new Tanh());
    }

    public Layer(RealMatrix weights, RealVector biases, UnivariateFunction activation) {
        this.weights = weights;
        this.biases = biases;
        this.activation = activation;
    }

    public RealVector transmit(RealVector input) {
        return weights.operate(input).add(biases).map(activation);
    }
}
