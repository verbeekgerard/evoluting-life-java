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
        ITransmitter[][] transmitters = new ITransmitter[neurons.size()][];

        for (int i=0; i<neurons.size(); i++) {
            TransmitResult neuronOutput = neurons.get(i).transmit();
            values.add(neuronOutput.getValue());
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