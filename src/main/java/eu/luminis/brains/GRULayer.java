package eu.luminis.brains;

import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.analysis.function.Tanh;
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
    private final GateLayer Gz;
    private final GateLayer Gr;
    private final GateLayer Gh;

    private RealVector state;

    public GRULayer(RealMatrix Wz, RealMatrix Uz, RealVector bz,
                    RealMatrix Wr, RealMatrix Ur, RealVector br,
                    RealMatrix Wh, RealMatrix Uh, RealVector bh) {
        this(Wz, Uz, bz,
            Wr, Ur, br,
            Wh, Uh, bh,
            new HardTanh());
    }

    public GRULayer(RealMatrix Wz, RealMatrix Uz, RealVector bz,
                    RealMatrix Wr, RealMatrix Ur, RealVector br,
                    RealMatrix Wh, RealMatrix Uh, RealVector bh, 
                    UnivariateFunction activation) {
        this.Gz = new GateLayer(Wz, Uz, bz);
        this.Gr = new GateLayer(Wr, Ur, br);
        this.Gh = new GateLayer(Wh, Uh, bh, activation);

        this.state = new ArrayRealVector(bz.getDimension());
    }

    public RealVector transmit(RealVector input) {
        /**
         * z_t = sigmoid(W_z . x_t + U_z . h_t-1 + b_z)
         * r_t = sigmoid(W_r . x_t + U_r . h_t-1 + b_r)
         * h_t = (1 - z_t) * h_t-1 + z_t * tanh(W_r . x_t + U_h . (r_t * h_t-1) + b_h)
        */
        RealVector zt = Gz.calculate(input, state);
        RealVector rt = Gr.calculate(input, state);
        RealVector ht = Gh.calculate(input, rt.ebeMultiply(state));

        ArrayRealVector one = new ArrayRealVector(state.getDimension(), 1.0);

        return state = one.subtract(zt).ebeMultiply(state).add(zt.ebeMultiply(ht));
    }
}
