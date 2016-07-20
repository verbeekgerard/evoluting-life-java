package brains;

import java.util.ArrayList;
import java.util.List;

import genetics.BrainGene;
import genetics.NeuronGene;

public class Brain {

	public List<Layer> layers = new ArrayList<>();
	
	public Brain(BrainGene gen){

		List<List<NeuronGene>> genLayers = gen.layers;

		// The output layer has no target layer
		Layer layer = new Layer(genLayers.get(0));
		layers.add(layer);

		// The other layers have target layers
		for (int i = 1; i < genLayers.size() - 1; i++) {
			layer = new Layer(genLayers.get(i), layers.get(layers.size() - 1));
			layers.add(layer);
		}

		// The input layer is of a different type
		layer = new InputLayer(genLayers.get(genLayers.size() - 1), layers.get(layers.size() - 1));
		layers.add(layer);
	}
	
	public List<Double> think(List<Double> input) {

		// Excite the input neurons
		InputLayer inputLayer = (InputLayer) layers.get(layers.size() - 1);
		inputLayer.sense(input);

		List<Double> output = null;

		for (int i = layers.size()-1;i>=0; i--) {
			Layer layer = layers.get(i);
			output = layer.transmit();
		}

		return output;
	}
	
}