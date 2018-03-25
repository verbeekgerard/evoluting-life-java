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
    private final RealVector gains;
    private final RealVector biases;
    private final UnivariateFunction activation;

    public GateLayer(RealMatrix weights, RealMatrix stateWeights, RealVector gains, RealVector biases) {
        this(weights, stateWeights, gains, biases, new Sigmoid());
    }

    public GateLayer(RealMatrix weights, RealMatrix stateWeights, RealVector gains, RealVector biases, UnivariateFunction activation) {
        this.weights = weights;
        this.stateWeights = stateWeights;
        this.gains = gains;
        this.biases = biases;
        this.activation = activation;
    }

    public RealVector calculate(RealVector input, RealVector state) {
        RealVector wSate = stateWeights.operate(state);
        RealVector wInput = weights.operate(input);

        RealVector sum = wInput.add(wSate);

        return normalize(sum).map(activation);
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
