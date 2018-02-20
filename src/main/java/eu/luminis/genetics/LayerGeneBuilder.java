package eu.luminis.genetics;

import eu.luminis.Options;
import eu.luminis.util.Range;
import org.apache.commons.math3.linear.*;

class LayerGeneBuilder {
    private int size;
    private double multiplier = new Range(-1 * Options.maxWeight.get(), Options.maxWeight.get()).random();

    private LayerGeneBuilder() {
    }

    public static LayerGeneBuilder create() {
        return new LayerGeneBuilder();
    }

    public LayerGeneBuilder withSize(int size) {
        this.size = size;
        return this;
    }

    public LayerGeneBuilder withMultiplier(double multiplier) {
        this.multiplier = multiplier;
        return this;
    }

    public LayerGene build() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(size).scalarMultiply(multiplier);
        RealVector biases = new ArrayRealVector(size);
        
        return new LayerGene(matrix.getData(), biases.toArray());
    }
}
