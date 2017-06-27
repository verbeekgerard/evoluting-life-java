package eu.luminis.brains;

import java.util.List;

import eu.luminis.genetics.NeuronGene;

class InputLayer extends Layer {

    public InputLayer(Neuron[] list) {
        super(list);
    }

    public InputLayer sense(List<Double> input) {
        // Excite the input neurons
        Neuron[] inputNeurons = this.getNeurons();

        for (int i = 0; i < inputNeurons.length; i++) {
            inputNeurons[i].excite(input.get(i));
        }

        return this;
    }
}