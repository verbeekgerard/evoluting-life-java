package eu.luminis.brains;

import eu.luminis.genetics.AxonGene;
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
                createTransmittingAxons(targetNeurons));
    }

    private List<Axon> createOutputAxons() {
        List<Axon> axons = new ArrayList<>();

        for (AxonGene axonGene : gene.getAxons()) {
            Axon axon = new Axon(axonGene.getStrength());
            axons.add(axon);
        }

        return axons;
    }

    private List<Axon> createTransmittingAxons(List<Neuron> targetNeurons) {
        List<Axon> axons = new ArrayList<>();

        for (int i = 0; i < gene.getAxons().size(); i++) {
            Axon axon = new Axon(gene.getAxons().get(i).getStrength(), targetNeurons.get(i));
            axons.add(axon);
        }

        return axons;
    }
}
