package eu.luminis.brains;

import eu.luminis.genetics.*;

import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.linear.*;

/**
 * Builds up a layer of the gated recurrent unit neural network
 */
class GRULayerBuilder {
    private GRULayerGene layerGene;

    private GRULayerBuilder() {
    }

    public static GRULayerBuilder create() {
        return new GRULayerBuilder();
    }

    public GRULayerBuilder withLayerGene(GRULayerGene layerGene) {
        this.layerGene = layerGene;
        return this;
    }

    public GRULayer build() {
        return buildWithFunction(new HardTanh());
    }

    public GRULayer buildAsOutput() {
        return buildWithFunction(new HardSigmoid());
    }

    private GRULayer buildWithFunction(UnivariateFunction function) {
        GateLayer updateLayer = new GateLayer(
            new Array2DRowRealMatrix(layerGene.getUpdateLayerGene().getWeights()),
            new Array2DRowRealMatrix(layerGene.getUpdateLayerGene().getStateWeights()),
            new ArrayRealVector(layerGene.getUpdateLayerGene().getGains()),
            new ArrayRealVector(layerGene.getUpdateLayerGene().getBiases()));

        GateLayer resetLayer = new GateLayer(
            new Array2DRowRealMatrix(layerGene.getResetLayerGene().getWeights()),
            new Array2DRowRealMatrix(layerGene.getResetLayerGene().getStateWeights()),
            new ArrayRealVector(layerGene.getResetLayerGene().getGains()),
            new ArrayRealVector(layerGene.getResetLayerGene().getBiases()));

        GateLayer outputLayer = new GateLayer(
            new Array2DRowRealMatrix(layerGene.getOutputLayerGene().getWeights()),
            new Array2DRowRealMatrix(layerGene.getOutputLayerGene().getStateWeights()),
            new ArrayRealVector(layerGene.getOutputLayerGene().getGains()),
            new ArrayRealVector(layerGene.getOutputLayerGene().getBiases()));

        return new GRULayer(updateLayer, resetLayer, outputLayer, function);
    }
}
