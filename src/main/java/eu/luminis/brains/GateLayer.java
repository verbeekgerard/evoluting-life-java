package eu.luminis.brains;

import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.analysis.function.Sigmoid;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.StatUtils;

/**
 * A simple gate for a layer of the neural network
 */
class GateLayer {
    private final RealMatrix weights;
    private final RealMatrix stateWeights;
    private final RealVector biases;
    private final UnivariateFunction activation;

    public GateLayer(RealMatrix weights, RealMatrix stateWeights, RealVector biases) {
        this(weights, stateWeights, biases, new Sigmoid());
    }

    public GateLayer(RealMatrix weights, RealMatrix stateWeights, RealVector biases, UnivariateFunction activation) {
        this.weights = weights;
        this.stateWeights = stateWeights;
        this.biases = biases;
        this.activation = activation;
    }

    public RealVector calculate(RealVector input, RealVector state) {
        RealVector wState = stateWeights.operate(state);
        RealVector wInput = weights.operate(input);

        return wInput.add(wState).add(biases).map(activation);
    }

    public RealVector calculateNormalized(RealVector input, RealVector state, RealVector gains) {
        RealVector wSate = stateWeights.operate(state);
        RealVector wInput = weights.operate(input);

        RealVector sum = wInput.add(wSate);

        return normalize(sum, gains).map(activation);
    }

    private RealVector normalize(RealVector samples, RealVector gains) {
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
