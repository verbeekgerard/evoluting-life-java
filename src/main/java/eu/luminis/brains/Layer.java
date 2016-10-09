package eu.luminis.brains;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.luminis.genetics.NeuronGene;

class Layer {

	private List<Neuron> neurons = new ArrayList<>();
	
	public Layer(List<NeuronGene> genomeLayer) {
		this(genomeLayer, null);
	}
	
	public Layer(List<NeuronGene> genomeLayer, Layer targetLayer) {
        List<Neuron> targetNeurons = targetLayer == null ?
                null :
                targetLayer.getNeurons();

        for (NeuronGene neuronGene : genomeLayer) {
            Neuron neuron = new Neuron(neuronGene, targetNeurons);
            neurons.add(neuron);
        }
	}
	
	public List<Neuron> getNeurons(){
        return neurons;
    }
	
	public List<Double> transmit() {
        return neurons.stream()
                .map(Neuron::transmit)
                .collect(Collectors.toList());
	}
}