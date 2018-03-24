package eu.luminis.genetics;

import eu.luminis.Options;
import eu.luminis.util.Range;
import org.apache.commons.math3.linear.*;

class GRULayerGeneBuilder {
    private int size;
    private double multiplier = new Range(-1 * Options.maxWeight.get(), Options.maxWeight.get()).random();

    private GRULayerGeneBuilder() {
    }

    public static GRULayerGeneBuilder create() {
        return new GRULayerGeneBuilder();
    }

    public GRULayerGeneBuilder withSize(int size) {
        this.size = size;
        return this;
    }

    public GRULayerGeneBuilder withMultiplier(double multiplier) {
        this.multiplier = multiplier;
        return this;
    }

    public GRULayerGene build() {
        return new GRULayerGene(buildGateLayerGene(), buildGateLayerGene(), buildGateLayerGene());
    }

    public GateLayerGene buildGateLayerGene() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(size).scalarMultiply(multiplier);
        RealMatrix stateWeights = MatrixUtils.createRealMatrix(size, size);
        RealVector biases = new ArrayRealVector(size);
        
        return new GateLayerGene(matrix.getData(), stateWeights.getData(), biases.toArray());
    }
}
