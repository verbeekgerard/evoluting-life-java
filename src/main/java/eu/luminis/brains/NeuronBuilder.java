package eu.luminis.brains;

import eu.luminis.genetics.NeuronGene;

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

    Neuron build(Neuron[] targetNeurons) {
        if (targetNeurons == null) return build();

        return new Neuron(
                gene.getThreshold(),
                gene.getRelaxation(),
                createTransmittingAxons(targetNeurons, 0));
    }

    void complement(Neuron neuron, Neuron[] targetNeurons) {
        if (neuron == null || targetNeurons == null) return;

        int offset = gene.getAxons().length - targetNeurons.length;
        Axon[] recurrentAxons = createTransmittingAxons(targetNeurons, offset);
        neuron.addRecurrentAxons(recurrentAxons);
    }

    private Axon[] createOutputAxons() {
        return new Axon[0];
    }

    private Axon[] createTransmittingAxons(Neuron[] targetNeurons, int offset) {
        Axon[] axons = new Axon[targetNeurons.length];

        for (int i = 0; i < targetNeurons.length; i++) {
            Axon axon = new Axon(gene.getAxons()[i + offset].getStrength(), targetNeurons[i]);
            axons[i] = axon;
        }

        return axons;
    }
}
