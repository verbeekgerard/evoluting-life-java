package eu.luminis.brains;

import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.analysis.function.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.*;

class Layer {
    private final RealMatrix weights;
    private final RealVector biases;
    private final RealMatrix stateWeights;
    private final RealVector gains;
    private final UnivariateFunction activation;

    private RealVector state;

    public Layer(RealMatrix weights, RealVector biases, RealMatrix stateWeights, RealVector gains) {
        this(weights, biases, stateWeights, gains, new Tanh());
    }

    public Layer(RealMatrix weights, RealVector biases, RealMatrix stateWeights, RealVector gains, UnivariateFunction activation) {
        this.weights = weights;
        this.biases = biases;
        this.stateWeights = stateWeights;
        this.gains = gains;
        this.activation = activation;

        this.state = new ArrayRealVector(biases.getDimension());
    }

    public RealVector transmit(RealVector input) {
        RealVector recurrentState = stateWeights.operate(state);
        RealVector summedInput = weights.operate(input).add(recurrentState);

        return state = normalize(summedInput).map(activation);
    }

    private RealVector normalize(RealVector samples) {
        double mu = mean(samples);
        double s = sigma(samples, mu);

        RealVector samplesCentered = samples.mapSubtract(mu);

        return gains.mapDivide(s).ebeMultiply(samplesCentered).add(biases);
    }

    private double sigma(RealVector samples, double mean) {
        return Math.sqrt(StatUtils.populationVariance(samples.toArray(), mean));
    }

    private double mean(RealVector samples) {
        return StatUtils.mean(samples.toArray());
    }
}
