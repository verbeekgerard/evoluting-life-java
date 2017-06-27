package eu.luminis.brains;

import com.sun.tools.javac.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

class Neuron {
    private Axon[] axons;
    private final double threshold;
    private final double relaxation;
    private double excitation = 0;

    Neuron(double threshold, double relaxation, Axon[] axons) {
        this.threshold = threshold;
        this.relaxation = relaxation;
        this.axons = axons;
    }

    public TransmitResult transmit() {
        if (excitation > threshold) {
            TransmitResult result = new TransmitResult(excitation, axons);
            excitation = 0;

            return result;
        }
        else {
            this.excitation = this.excitation > 0 ?
                    this.excitation * (1 - this.relaxation) :
                    0;

            return new TransmitResult(0, new Axon[0]);
        }
    }

    public void excite(double value) {
        this.excitation += value;
    }

    void addRecurrentAxons(Axon[] axons) {
        System.arraycopy(axons, 0, this.axons, this.axons.length, axons.length);
    }
}