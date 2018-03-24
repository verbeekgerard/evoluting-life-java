package eu.luminis.brains;

import org.apache.commons.math3.analysis.*;

public final class HardTanh implements UnivariateFunction {
    public double value(double x) {
        return x < -1.0 ? -1.0 : x > 1.0 ? 1.0 : x;
    }
}
