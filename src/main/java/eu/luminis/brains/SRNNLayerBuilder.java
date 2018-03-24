package eu.luminis.brains;

import eu.luminis.genetics.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.*;
import org.apache.commons.math3.linear.*;

/**
 * Builds up a layer of the simple recurrent neural network
 */
class SRNNLayerBuilder {
    private SRNNLayerGene layerGene;

    private SRNNLayerBuilder() {
    }

    public static SRNNLayerBuilder create() {
        return new SRNNLayerBuilder();
    }

    public SRNNLayerBuilder withLayerGene(SRNNLayerGene layerGene) {
        this.layerGene = layerGene;
        return this;
    }

    public SRNNLayer build() {
        return buildWithFunction(new HardTanh());
    }

    public SRNNLayer buildAsOutput() {
        return buildWithFunction(new Sigmoid());
    }

    private SRNNLayer buildWithFunction(UnivariateFunction function) {
        RealMatrix weights = new Array2DRowRealMatrix(layerGene.getWeights());
        RealVector biases = new ArrayRealVector(layerGene.getBiases());
        RealMatrix stateWeights = new Array2DRowRealMatrix(layerGene.getStateWeights());
        RealVector gains = new ArrayRealVector(layerGene.getGains());

        return new SRNNLayer(weights, biases, stateWeights, gains, function);
    }
}
