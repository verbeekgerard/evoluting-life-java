package eu.luminis.brains;

import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.linear.*;

/**
 * Definitions
 * -----------
 * x_t: input vector
 * h_t: output vector
 * z_t: update gate vector
 * r_t: reset gate vector
 * W, U and b parameter matrices and vector
 * 
 * '*': element wise product
 * '.': dot product
 * 
 * Functions
 * ---------
 * z_t = sigmoid(W_z . x_t + U_z . h_t-1 + b_z)
 * r_t = sigmoid(W_r . x_t + U_r . h_t-1 + b_r)
 * h_t = (1 - z_t) * h_t-1 + z_t * tanh(W_r . x_t + U_h . (r_t * h_t-1) + b_h)
 * 
 * Parameters
 * ----------
 * W_z, U_z, b_z
 * W_r, U_r, b_r
 * W_h, U_h, b_h
 */

/**
 * A layer of the gated recurrent unit neural network
 */
class GRULayer implements ILayer {
    private final GateLayer updateLayer;
    private final GateLayer resetLayer;
    private final GateLayer outputLayer;

    private RealVector state;

    public GRULayer(GateLayer updateLayer,
                    GateLayer resetLayer,
                    GateLayer outputLayer) {
        this(updateLayer, resetLayer, outputLayer, new HardTanh());
    }

    public GRULayer(GateLayer updateLayer,
                    GateLayer resetLayer,
                    GateLayer outputLayer, 
                    UnivariateFunction activation) {
        this.updateLayer = updateLayer;
        this.resetLayer = resetLayer;
        this.outputLayer = outputLayer;

        this.state = new ArrayRealVector(updateLayer.getWidth());
    }

    public RealVector transmit(RealVector input) {
        /**
         * z_t = sigmoid(W_z . x_t + U_z . h_t-1 + b_z)
         * r_t = sigmoid(W_r . x_t + U_r . h_t-1 + b_r)
         * h'_t = tanh(W_r . x_t + U_h . (r_t * h_t-1) + b_h)
         * 
         * One of both:
         * h_t = (1 - z_t) * h_t-1 + z_t * h'_t
         * h_t = z_t * h_t-1 + (1 - z_t) * h'_t
        */
        RealVector zt = updateLayer.calculate(input, state);
        RealVector rt = resetLayer.calculate(input, state);
        RealVector ht = outputLayer.calculate(input, rt.ebeMultiply(state));

        ArrayRealVector one = new ArrayRealVector(state.getDimension(), 1.0);

        // when z=1 and r=1, then we have the SRNN
        return state = one.subtract(zt).ebeMultiply(state).add(zt.ebeMultiply(ht));
    }
}
