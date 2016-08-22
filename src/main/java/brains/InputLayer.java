package brains;

import java.util.List;

import genetics.NeuronGene;

public class InputLayer extends Layer {

	public InputLayer(List<NeuronGene> list, Layer targetLayer){
		super(list, targetLayer);
	}

	public void sense(List<Double> input) {
        // Excite the input neurons
        List<Neuron> inputNeurons = this.getNeurons();

        for (int i = inputNeurons.size()-1;i>=0; i--) {
        	inputNeurons.get(i).excite(input.get(i));
        }
	}
}