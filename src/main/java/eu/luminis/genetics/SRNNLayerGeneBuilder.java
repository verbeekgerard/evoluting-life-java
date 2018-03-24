package eu.luminis.genetics;

import eu.luminis.Options;
import eu.luminis.util.Range;
import org.apache.commons.math3.linear.*;

class SRNNLayerGeneBuilder {
    private int size;
    private double multiplier = new Range(-1 * Options.maxWeight.get(), Options.maxWeight.get()).random();

    private SRNNLayerGeneBuilder() {
    }

    public static SRNNLayerGeneBuilder create() {
        return new SRNNLayerGeneBuilder();
    }

    public SRNNLayerGeneBuilder withSize(int size) {
        this.size = size;
        return this;
    }

    public SRNNLayerGeneBuilder withMultiplier(double multiplier) {
        this.multiplier = multiplier;
        return this;
    }

    public SRNNLayerGene build() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(size).scalarMultiply(multiplier);
        RealVector biases = new ArrayRealVector(size);
        RealMatrix stateWeights = MatrixUtils.createRealMatrix(size, size);
        RealVector gains = new ArrayRealVector(size);
        
        return new SRNNLayerGene(matrix.getData(), biases.toArray(), stateWeights.getData(), gains.toArray());
    }
}