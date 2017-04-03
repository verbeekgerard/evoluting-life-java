package eu.luminis.brains;

import java.util.ArrayList;
import java.util.List;

import eu.luminis.genetics.NeuronGene;

class Layer {

    private final List<Neuron> neurons = new ArrayList<>();

    public Layer(List<NeuronGene> layerGenes) {
        this(layerGenes, null, false);
    }

    public Layer(List<NeuronGene> layerGenes, Layer targetLayer, boolean recurrent) {
        List<Neuron> targetNeurons = targetLayer == null ?
                null :
                targetLayer.getNeurons();

        for (NeuronGene neuronGene : layerGenes) {
            NeuronBuilder builder = new NeuronBuilder(neuronGene);
            Neuron neuron = builder.build(targetNeurons);
            neurons.add(neuron);
        }

        if (!recurrent) return;

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