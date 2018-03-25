package eu.luminis.brains;

import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.linear.*;

/**
 * A layer of the simple recurrent neural network
 */
class SRNNLayer implements ILayer {
    private final GateLayer gateLayer;
    private RealVector state;

    public SRNNLayer(RealMatrix weights, RealVector biases, RealMatrix stateWeights, RealVector gains) {
        this(weights, biases, stateWeights, gains, new HardTanh());
    }

    public SRNNLayer(RealMatrix weights, RealVector biases, RealMatrix stateWeights, RealVector gains, UnivariateFunction activation) {
        this.gateLayer = new GateLayer(weights, stateWeights, gains, biases, activation);
        this.state = new ArrayRealVector(biases.getDimension());
    }

    public RealVector transmit(RealVector input) {
        return state = gateLayer.calculate(input, state);
    }
}
