package eu.luminis.brains;

import org.apache.commons.math3.linear.*;

/**
 * Builds up a layer of the neural network
 */
class WeightsBuilder {
    private double[][] matrixData;

    private WeightsBuilder() {
    }

    public static WeightsBuilder create() {
        return new WeightsBuilder();
    }

    public WeightsBuilder withMatrixData(double[][] matrixData) {
        this.matrixData = matrixData;
        return this;
    }

    public RealMatrix build() {
        return MatrixUtils.createRealMatrix(matrixData);
    }
}
