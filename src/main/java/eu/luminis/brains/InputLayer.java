package eu.luminis.brains;

class InputLayer extends Layer {

    public InputLayer(Neuron[] list) {
        super(list);
    }

    public InputLayer sense(double[] input) {
        // Excite the input neurons
        Neuron[] inputNeurons = this.getNeurons();

        for (int i = 0; i < inputNeurons.length; i++) {
            inputNeurons[i].excite(input[i]);
        }

        return this;
    }
}