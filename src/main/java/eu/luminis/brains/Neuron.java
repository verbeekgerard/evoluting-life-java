package eu.luminis.brains;

import java.util.ArrayList;
import java.util.List;

class Neuron {
    private final List<Axon> axons;
    private final double threshold;
    private final double relaxation;
    private double excitation = 0;

    Neuron(double threshold, double relaxation, List<Axon> axons) {
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

            return new TransmitResult(0, new ArrayList<>());
        }
    }

    public void excite(double value) {
        this.excitation += value;
    }

    void addRecurrentAxons(List<Axon> axons) {
        this.axons.addAll(axons);
    }
}