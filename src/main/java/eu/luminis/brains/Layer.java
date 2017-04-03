package eu.luminis.brains;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.luminis.genetics.NeuronGene;

class Layer {

    private final List<Neuron> neurons = new ArrayList<>();

    public Layer(List<NeuronGene> layerGenes) {
        this(layerGenes, null);
    }

    public Layer(List<NeuronGene> layerGenes, Layer targetLayer) {
        List<Neuron> targetNeurons = targetLayer == null ?
                null :
                targetLayer.getNeurons();

        for (NeuronGene neuronGene : layerGenes) {
            NeuronBuilder builder = new NeuronBuilder(neuronGene);
            Neuron neuron = builder.build(targetNeurons);
            neurons.add(neuron);
        }

        int i = 0;
        for (NeuronGene neuronGene : layerGenes) {
            NeuronBuilder builder = new NeuronBuilder(neuronGene);

            Neuron neuron = neurons.get(i++);
            builder.complement(neuron, neurons);
        }
    }

    public List<Neuron> getNeurons() {
        return neurons;
    }

    public List<Double> transmit() {
        List<Double> values = new ArrayList<>();
        List<ITransmitter> transmitters = new ArrayList<>();

        for (Neuron neuron : neurons) {
            Neuron.TransmitResult neuronOutput = neuron.transmit();
            values.add(neuronOutput.getValue());
            transmitters.addAll(neuronOutput.getTransmitters());
        }

        for (ITransmitter transmitter : transmitters) {
            transmitter.transmit();
        }

        return values;
    }
}