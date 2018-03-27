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
        return new GRULayerGene(buildGateLayerGene(1.5), buildGateLayerGene(1.5), buildGateLayerGene());
    }

    private GateLayerGene buildGateLayerGene() {
        return buildGateLayerGene(0.0);
    }

    private GateLayerGene buildGateLayerGene(double biasOffset) {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(size).scalarMultiply(multiplier);
        RealMatrix stateWeights = MatrixUtils.createRealMatrix(size, size);
        RealVector gains = new ArrayRealVector(size);
        RealVector biases = new ArrayRealVector(size, biasOffset);
        
        return new GateLayerGene(matrix.getData(), stateWeights.getData(), gains.toArray(), biases.toArray(), biasOffset);
    }
}
