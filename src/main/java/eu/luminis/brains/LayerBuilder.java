package eu.luminis.brains;

import eu.luminis.genetics.NeuronGene;

import java.util.List;

/**
 * Builds up a layer of the neural network
 */
class LayerBuilder {
    private Layer targetLayer;
    private List<NeuronGene> layerGenes;
    private boolean isRecurrent = false;

    private LayerBuilder() {
    }

    public static LayerBuilder layer() {
        return new LayerBuilder();
    }

    public LayerBuilder withNeuronGenes(List<NeuronGene> layerGenes) {
        this.layerGenes = layerGenes;
        return this;
    }

    public LayerBuilder withTargetLayer(Layer targetLayer) {
        this.targetLayer = targetLayer;
        return this;
    }

    public LayerBuilder withRecurrence(boolean recurrence) {
        isRecurrent = recurrence;
        return this;
    }

    public Layer build() {
        Neuron[] neurons = buildNeuronList();
        return new Layer(neurons);
    }

    public InputLayer buildAsInput() {
        Neuron[] neurons = buildNeuronList();
        return new InputLayer(neurons);
    }

    private Neuron[] buildNeuronList() {
        Neuron[] neurons = new Neuron[layerGenes.size()];

        Neuron[] targetNeurons = targetLayer == null ?
                null :
                targetLayer.getNeurons();

        for (int i=0; i<layerGenes.size(); i++) {
            NeuronBuilder builder = new NeuronBuilder(layerGenes.get(i));
            Neuron neuron = builder.build(targetNeurons);
            neurons[i] = neuron;
        }

        if (!isRecurrent) return neurons;

        int i = 0;
        for (NeuronGene neuronGene : layerGenes) {
            NeuronBuilder builder = new NeuronBuilder(neuronGene);

            Neuron neuron = neurons[i++];
            builder.complement(neuron, neurons);
        }

        return neurons;
    }
}
