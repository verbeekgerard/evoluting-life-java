package eu.luminis.brains;

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

    public double transmit() {
        if (this.excitation > this.threshold) {
            double excitation = this.excitation;
            this.excitation = 0;

            axons.forEach(Axon::transmit);

            return excitation;
        }
        else {
            this.excitation = this.excitation > 0 ?
                    this.excitation * (1 - this.relaxation) :
                    0;

            return 0;
        }
    }

    public void excite(double value) {
        this.excitation += value;
    }
}