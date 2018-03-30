package eu.luminis.genetics;

import eu.luminis.Options;
import eu.luminis.util.Range;
import org.apache.commons.math3.linear.*;

class SRNNLayerGeneBuilder {
    private int rows;
    private int columns;
    private double multiplier = new Range(-1 * Options.maxWeight.get(), Options.maxWeight.get()).random();

    private double biasOffset = 0.0;

    private SRNNLayerGeneBuilder() {
    }

    public static SRNNLayerGeneBuilder create() {
        return new SRNNLayerGeneBuilder();
    }

    public SRNNLayerGeneBuilder withSize(int size) {
        this.rows = size;
        this.columns = size;
        return this;
    }

    public SRNNLayerGeneBuilder withSize(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        return this;
    }

    public SRNNLayerGeneBuilder withMultiplier(double multiplier) {
        this.multiplier = multiplier;
        return this;
    }

    public SRNNLayerGeneBuilder withBiasOffset(double offset) {
        this.biasOffset = offset;
        return this;
    }

    public SRNNLayerGene build() {
        return new SRNNLayerGene(rows, columns, biasOffset);
    }

    public SRNNLayerGene builIdentity() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(rows).scalarMultiply(multiplier);
        RealMatrix stateWeights = MatrixUtils.createRealMatrix(rows, columns);
        RealVector gains = new ArrayRealVector(rows);
        RealVector biases = new ArrayRealVector(rows);
        
        return new SRNNLayerGene(matrix.getData(), stateWeights.getData(), gains.toArray(), biases.toArray());
    }
}
