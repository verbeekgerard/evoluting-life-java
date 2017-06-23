package eu.luminis.brains;

import java.util.ArrayList;
import java.util.List;

import eu.luminis.genetics.NeuronGene;

class Layer {

    private final List<Neuron> neurons;

    public Layer(List<Neuron> neurons) {
        this.neurons = neurons;
    }

    public List<Neuron> getNeurons() {
        return neurons;
    }

    public List<Double> transmit() {
        List<Double> values = new ArrayList<>();
        List<ITransmitter> transmitters = new ArrayList<>();

        for (Neuron neuron : neurons) {
            TransmitResult neuronOutput = neuron.transmit();
            values.add(neuronOutput.getValue());
            transmitters.addAll(neuronOutput.getTransmitters());
        }

        for (ITransmitter transmitter : transmitters) {
            transmitter.transmit();
        }

        return values;
    }
}