package eu.luminis.brains;

import org.apache.commons.math3.analysis.*;

public final class ReLU implements UnivariateFunction {
    public double value(double x) {
        return x < 0.0 ? 0.0 : x;
    }
}
