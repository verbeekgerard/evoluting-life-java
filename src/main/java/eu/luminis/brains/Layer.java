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
    }

    public List<Neuron> getNeurons() {
        return neurons;
    }

    public List<Double> transmit() {
        return neurons.stream()
                .map(Neuron::transmit)
                .collect(Collectors.toList());
    }
}