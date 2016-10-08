package eu.luminis.brains;

import java.util.ArrayList;
import java.util.List;

import eu.luminis.genetics.BrainGene;
import eu.luminis.genetics.NeuronGene;

public class Brain {
	private List<Layer> layers = new ArrayList<>();
	private InputLayer inputLayer;
	
	public Brain(BrainGene gen) {
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
		this.inputLayer = new InputLayer(genLayers.get(genLayers.size() - 1), layers.get(layers.size() - 1));
	}
	
	public List<Double> think(List<Double> input) {

		// Excite the input neurons
		this.inputLayer.sense(input);

		List<Double> output = this.inputLayer.transmit();

		for (int i = layers.size()-1; i >= 0; i--) {
			Layer layer = layers.get(i);
			output = layer.transmit();
		}
		
		return output;
	}
	
}