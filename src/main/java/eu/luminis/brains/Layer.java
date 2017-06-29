package eu.luminis.brains;

class Layer {

    private final Neuron[] neurons;

    public Layer(Neuron[] neurons) {
        this.neurons = neurons;
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public double[] transmit() {
        double[] values = new double[neurons.length];
        ITransmitter[][] transmitters = new ITransmitter[neurons.length][];

        for (int i=0; i<neurons.length; i++) {
            TransmitResult<Axon> neuronOutput = neurons[i].transmit();
            values[i] = neuronOutput.getValue();
            transmitters[i] = neuronOutput.getTransmitters();
        }

        for (int i=0; i<transmitters.length; i++) {
            for (int j=0; j<transmitters[i].length; j++) {
                transmitters[i][j].transmit();
            }
        }

        return values;
    }
}