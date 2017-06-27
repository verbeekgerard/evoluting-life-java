package eu.luminis.brains;

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

    public TransmitResult<Axon> transmit() {
        if (excitation > threshold) {
            TransmitResult<Axon> result = new TransmitResult<>(excitation, axons);
            excitation = 0;

            return result;
        }
        else {
            this.excitation = this.excitation > 0 ?
                    this.excitation * (1 - this.relaxation) :
                    0;

            return new TransmitResult<>(0, new Axon[0]);
        }
    }

    public void excite(double value) {
        this.excitation += value;
    }

    void addRecurrentAxons(Axon[] axons) {
        Axon[] all = new Axon[this.axons.length + axons.length];
        System.arraycopy(this.axons, 0, all, 0, this.axons.length);
        System.arraycopy(axons, 0, all, this.axons.length, axons.length);

        this.axons = all;
    }
}