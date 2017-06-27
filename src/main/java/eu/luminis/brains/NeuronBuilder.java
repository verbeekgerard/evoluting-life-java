package eu.luminis.brains;

import eu.luminis.genetics.NeuronGene;

import java.util.ArrayList;
import java.util.List;

class NeuronBuilder {
    private NeuronGene gene;

    NeuronBuilder(NeuronGene neuronGene) {
        this.gene = neuronGene;
    }

    Neuron build() {
        return new Neuron(
                gene.getThreshold(),
                gene.getRelaxation(),
                createOutputAxons());
    }

    Neuron build(List<Neuron> targetNeurons) {
        if (targetNeurons == null) return build();

        return new Neuron(
                gene.getThreshold(),
                gene.getRelaxation(),
                createTransmittingAxons(targetNeurons, 0));
    }

    void complement(Neuron neuron, List<Neuron> targetNeurons) {
        if (neuron == null || targetNeurons == null) return;

        int offset = gene.getAxons().size() - targetNeurons.size();
        Axon[] recurrentAxons = createTransmittingAxons(targetNeurons, offset);
        neuron.addRecurrentAxons(recurrentAxons);
    }

    private Axon[] createOutputAxons() {
        return new Axon[0];
    }

    private Axon[] createTransmittingAxons(List<Neuron> targetNeurons, int offset) {
        Axon[] axons = new Axon[targetNeurons.size()];

        for (int i = 0; i < targetNeurons.size(); i++) {
            Axon axon = new Axon(gene.getAxons().get(i + offset).getStrength(), targetNeurons.get(i));
            axons[i] = axon;
        }

        return axons;
    }
}
