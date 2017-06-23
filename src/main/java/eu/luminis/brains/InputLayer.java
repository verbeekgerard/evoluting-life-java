package eu.luminis.brains;

import java.util.List;

import eu.luminis.genetics.NeuronGene;

class InputLayer extends Layer {

    public InputLayer(List<Neuron> list) {
        super(list);
    }

    public InputLayer sense(List<Double> input) {
        // Excite the input neurons
        List<Neuron> inputNeurons = this.getNeurons();

        for (int i = 0; i < inputNeurons.size(); i++) {
            inputNeurons.get(i).excite(input.get(i));
        }

        return this;
    }
}