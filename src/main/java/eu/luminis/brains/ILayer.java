package eu.luminis.brains;

import org.apache.commons.math3.linear.RealVector;

public interface ILayer {
    RealVector transmit(RealVector input);
}
