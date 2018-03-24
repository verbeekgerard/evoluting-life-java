package eu.luminis.brains;

import org.apache.commons.math3.analysis.*;

public final class HardSigmoid implements UnivariateFunction {
    public double value(double x) {
        return x < -0.5 ? 0.0 : x > 0.5 ? 1.0 : x + 0.5;
    }
}
