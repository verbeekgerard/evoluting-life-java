package eu.luminis.brains;

import java.util.List;

class TransmitResult {
    private final double value;
    private final List<? extends ITransmitter> transmitters;

    TransmitResult(double value, List<? extends ITransmitter> transmitters) {

        this.value = value;
        this.transmitters = transmitters;
    }

    public double getValue() {
        return value;
    }

    public List<? extends ITransmitter> getTransmitters() {
        return transmitters;
    }
}
