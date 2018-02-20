package eu.luminis.brains;

import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.analysis.function.*;
import org.apache.commons.math3.linear.*;

class Layer {
    private final RealMatrix weights;
    private final RealVector biases;
    private final RealMatrix stateWeights;
    private final UnivariateFunction activation;

    private RealVector state;

    public Layer(RealMatrix weights, RealVector biases, RealMatrix stateWeights) {
        this(weights, biases, stateWeights, new Tanh());
    }

    public Layer(RealMatrix weights, RealVector biases, RealMatrix stateWeights, UnivariateFunction activation) {
        this.weights = weights;
        this.biases = biases;
        this.stateWeights = stateWeights;
        this.activation = activation;

        this.state = new ArrayRealVector(biases.getDimension());
    }

    public RealVector transmit(RealVector input) {
        RealVector recurrentState = stateWeights.operate(state);
        state = weights.operate(input).add(biases).add(recurrentState).map(activation);
        
        return state;
    }
}
