package eu.luminis.brains;

class TransmitResult<T extends ITransmitter> {
    private final double value;
    private final T[] transmitters;

    TransmitResult(double value, T[] transmitters) {

        this.value = value;
        this.transmitters = transmitters;
    }

    public double getValue() {
        return value;
    }

    public T[] getTransmitters() {
        return transmitters;
    }
}
