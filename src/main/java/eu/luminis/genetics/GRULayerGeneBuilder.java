package eu.luminis.genetics;

import eu.luminis.Options;
import eu.luminis.util.Range;
import org.apache.commons.math3.linear.*;

class GRULayerGeneBuilder {
    private int rows;
    private int columns;
    private double multiplier = new Range(-1 * Options.maxWeight.get(), Options.maxWeight.get()).random();
    private double resetBiasOffset = 1.0;
    private double updateBiasOffset = 1.0;

    private GRULayerGeneBuilder() {
    }

    public static GRULayerGeneBuilder create() {
        return new GRULayerGeneBuilder();
    }

    public GRULayerGeneBuilder withSize(int size) {
        this.rows = size;
        this.columns = size;
        return this;
    }

    public GRULayerGeneBuilder withSize(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        return this;
    }

    public GRULayerGeneBuilder withMultiplier(double multiplier) {
        this.multiplier = multiplier;
        return this;
    }

    public GRULayerGeneBuilder withResetBiasOffset(double offset) {
        this.resetBiasOffset = offset;
        return this;
    }

    public GRULayerGeneBuilder withUpdateBiasOffset(double offset) {
        this.updateBiasOffset = offset;
        return this;
    }

    public GRULayerGene build() {
        return new GRULayerGene(
            new SRNNLayerGene(rows, columns, updateBiasOffset),
            new SRNNLayerGene(rows, columns, resetBiasOffset),
            new SRNNLayerGene(rows, columns));
    }

    public GRULayerGene builIdentity() {
        return new GRULayerGene(
            buildIdentityGateLayerGene(updateBiasOffset),
            buildIdentityGateLayerGene(resetBiasOffset),
            buildIdentityGateLayerGene());
    }

    private SRNNLayerGene buildIdentityGateLayerGene() {
        return buildIdentityGateLayerGene(0.0);
    }

    private SRNNLayerGene buildIdentityGateLayerGene(double biasOffset) {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(rows).scalarMultiply(multiplier);
        RealMatrix stateWeights = MatrixUtils.createRealMatrix(rows, columns);
        RealVector gains = new ArrayRealVector(rows);
        RealVector biases = new ArrayRealVector(rows, biasOffset);
        
        return new SRNNLayerGene(matrix.getData(), stateWeights.getData(), gains.toArray(), biases.toArray(), biasOffset);
    }
}
