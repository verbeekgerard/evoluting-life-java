package eu.luminis.genetics;

import org.apache.commons.math3.linear.*;

class LayerGeneBuilder {
    private int size;

    private LayerGeneBuilder() {
    }

    public static LayerGeneBuilder create() {
        return new LayerGeneBuilder();
    }

    public LayerGeneBuilder withSize(int size) {
        this.size = size;
        return this;
    }

    public LayerGene build() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(size);
        RealVector biases = new ArrayRealVector(size);
        
        return new LayerGene(matrix.getData(), biases.toArray());
    }
}
