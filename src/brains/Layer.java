package brains;

import java.util.ArrayList;
import java.util.List;

import genetics.NeuronGene;

public class Layer {

	protected List<Neuron> neurons = new ArrayList<>();
	
	public Layer(List<NeuronGene> genomeLayer){
		this(genomeLayer, null);
	}
	
	public Layer(List<NeuronGene> genomeLayer, Layer targetLayer){
        List<Neuron> targetNeurons = targetLayer == null ? null : targetLayer.getNeurons();
        for (int i=0;i < genomeLayer.size();i++) { 
            neurons.add(new Neuron(genomeLayer.get(i), targetNeurons));
        }
	}
	
	public List<Neuron> getNeurons(){
        return neurons;
    }
	
	public List<Double> transmit(){

        List<Double> excitations = new ArrayList<>();
        List<Neuron> neurons = this.getNeurons();
        for (int i=0; i< neurons.size(); i++) {
            excitations.add(
                neurons.get(i).transmit()
            );
        }

        return excitations;
	}
}
