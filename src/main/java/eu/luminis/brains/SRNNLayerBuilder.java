package eu.luminis.brains;

import eu.luminis.genetics.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.linear.*;

/**
 * Builds up a layer of the simple recurrent neural network
 */
class SRNNLayerBuilder {
    private SRNNLayerGene gene;
    private UnivariateFunction activation = new HardTanh();

    private SRNNLayerBuilder() {
    }

    public static SRNNLayerBuilder create() {
        return new SRNNLayerBuilder();
    }

    public SRNNLayerBuilder withGene(SRNNLayerGene gene) {
        this.gene = gene;
        return this;
    }

    public SRNNLayerBuilder withActivation(UnivariateFunction activation) {
        this.activation = activation;
        return this;
    }

    public SRNNLayer build() {
        RealMatrix weights = new Array2DRowRealMatrix(gene.getWeights());
        RealVector biases = new ArrayRealVector(gene.getBiases());
        RealMatrix stateWeights = new Array2DRowRealMatrix(gene.getStateWeights());
        RealVector gains = new ArrayRealVector(gene.getGains());

        return new SRNNLayer(weights, biases, stateWeights, gains, activation);
    }
}
