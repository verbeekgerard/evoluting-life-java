package eu.luminis.brains;

import eu.luminis.genetics.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.*;
import org.apache.commons.math3.linear.*;

/**
 * Builds up a layer of the neural network
 */
class LayerBuilder {
    private LayerGene layerGene;

    private LayerBuilder() {
    }

    public static LayerBuilder create() {
        return new LayerBuilder();
    }

    public LayerBuilder withLayerGene(LayerGene layerGene) {
        this.layerGene = layerGene;
        return this;
    }

    public Layer build() {
        return buildWithFunction(new Tanh());
    }

    public Layer buildAsOutput() {
        return buildWithFunction(new Sigmoid());
    }

    private Layer buildWithFunction(UnivariateFunction function) {
        RealMatrix weights = new Array2DRowRealMatrix(layerGene.getWeights());
        RealVector biases = new ArrayRealVector(layerGene.getBiases());

        return new Layer(weights, biases, function);
    }
}
