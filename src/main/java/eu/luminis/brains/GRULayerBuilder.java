package eu.luminis.brains;

import eu.luminis.genetics.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.*;
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
        return buildWithFunction(new Sigmoid());
    }

    private GRULayer buildWithFunction(UnivariateFunction function) {
        RealMatrix Wz = new Array2DRowRealMatrix(layerGene.getGz().getWeights());
        RealMatrix Uz = new Array2DRowRealMatrix(layerGene.getGz().getStateWeights());
        RealVector gz = new ArrayRealVector(layerGene.getGz().getGains());
        RealVector bz = new ArrayRealVector(layerGene.getGz().getBiases());

        RealMatrix Wr = new Array2DRowRealMatrix(layerGene.getGr().getWeights());
        RealMatrix Ur = new Array2DRowRealMatrix(layerGene.getGr().getStateWeights());
        RealVector gr = new ArrayRealVector(layerGene.getGr().getGains());
        RealVector br = new ArrayRealVector(layerGene.getGr().getBiases());

        RealMatrix Wh = new Array2DRowRealMatrix(layerGene.getGh().getWeights());
        RealMatrix Uh = new Array2DRowRealMatrix(layerGene.getGh().getStateWeights());
        RealVector gh = new ArrayRealVector(layerGene.getGh().getGains());
        RealVector bh = new ArrayRealVector(layerGene.getGh().getBiases());

        return new GRULayer(Wz, Uz, gz, bz,
                            Wr, Ur, gr, br,
                            Wh, Uh, gh, bh,
                            function);
    }
}
