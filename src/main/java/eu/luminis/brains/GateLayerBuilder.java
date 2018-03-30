package eu.luminis.brains;

import eu.luminis.genetics.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.linear.*;

/**
 * Builds up a layer of the gated recurrent unit neural network
 */
class GateLayerBuilder {
    private SRNNLayerGene gene;
    private UnivariateFunction activation = new HardSigmoid();

    private GateLayerBuilder() {
    }

    public static GateLayerBuilder create() {
        return new GateLayerBuilder();
    }

    public GateLayerBuilder withGene(SRNNLayerGene gene) {
        this.gene = gene;
        return this;
    }

    public GateLayerBuilder withActivation(UnivariateFunction activation) {
        this.activation = activation;
        return this;
    }

    public GateLayer build() {
        return new GateLayer(
            new Array2DRowRealMatrix(gene.getWeights()),
            new Array2DRowRealMatrix(gene.getStateWeights()),
            new ArrayRealVector(gene.getGains()),
            new ArrayRealVector(gene.getBiases()),
            activation);
    }
}
