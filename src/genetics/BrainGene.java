package genetics;

import java.util.ArrayList;
import java.util.List;

import brains.Layer;
import main.Option;
import main.Options;
import util.Range;

public class BrainGene {
	
	public List<List<NeuronGene>> layers = new ArrayList<>();
	
	
	public BrainGene(int inputCount, int outputCount){
		// Construct the output layer
		layers.add( createLayer(0, outputCount) );

		// Construct the hidden layers
		double minGenesPerHL = Math.max(inputCount, outputCount);
		double numberOfHL = Math.floor(new Range(Options.minHiddenLayers.get(), Options.maxHiddenLayers.get()).random());
		for (int i=0; i<numberOfHL; i++) {

			int maxTargetCount = layers.get(layers.size() - 1).size();
			int neuronCount = (int) Math.floor(new Range(minGenesPerHL, Options.maxNeuronsPerLayer.get()).random());

			layers.add( createLayer(maxTargetCount, neuronCount) );
		}

		int maxTargetCount = layers.get(layers.size() - 1).size();

		// Construct the input layer
		layers.add( createLayer(maxTargetCount, inputCount) );		
	}
	
	public BrainGene(List<List<NeuronGene>> stateLayers){

		for(int i=0; i<stateLayers.size(); i++) {
			List<NeuronGene> layer = new ArrayList<>();
			
			List<NeuronGene> stateLayer = stateLayers.get(i);
			
			for(int j=0; j < stateLayer.size(); j++) {
				NeuronGene neuronGene = stateLayer.get(j);
				layer.add(new NeuronGene(neuronGene.threshold, neuronGene.relaxation, neuronGene.axons)); // TODO Should deep copy?
			}
			layers.add(layer);
		}
	}
	
	public List<NeuronGene> createLayer(int maxTargetCount, int neuronCount) {
		List<NeuronGene> neuronGenes = new ArrayList<>();
		for (int i=0;i<neuronCount;i++) {
			neuronGenes.add( new NeuronGene(maxTargetCount) );
		}
		return neuronGenes;
	}

	public void mutate(){

		for (int i=0; i<layers.size(); i++) {
			
			List<NeuronGene> stateLayer = layers.get(i);
			
			for (int j=0; j<stateLayer.size(); j++) {
				if (Math.random() < Options.geneReplacementRate.get()) {
					int targetCount = (i==0) ? 0 : layers.get(i-1).size();
					stateLayer.set(j, new NeuronGene(targetCount));
					continue;
				}

				if (Math.random() < Options.geneMutationRate.get()) {
					stateLayer.get(j).mutate();
				}
			}
		}
	}

//	public BrainGene clone(){
//		return new BrainGene(this.layers); // TODO deep copy is this used?
//	}
	
	public List<BrainGene> createChildClones(BrainGene a, BrainGene b) {
		List<BrainGene> childs = new ArrayList<>();
		
		if (Math.random() < 0.5){
			childs.add(a);
			childs.add(b);
		} else {
			childs.add(b);
			childs.add(a);
		}
				
		return childs;
	}
	
	public List<List<NeuronGene>> getEmptyChild(int layerCount) {
		List<List<NeuronGene>> stateLayers = new ArrayList<List<NeuronGene>>();
		for (int j=0; j<layerCount; j++) {
			stateLayers.add(new ArrayList<NeuronGene>());
		}
		return stateLayers;
	};

	public List<BrainGene> mate(BrainGene partner){
		List<List<NeuronGene>> a = this.layers;
		List<List<NeuronGene>> b = partner.layers;

		if (a.size() != b.size()) {
			return createChildClones(this, partner);
		}
		
		List<BrainGene> children = new ArrayList<>();
		children.add(new BrainGene(getEmptyChild(a.size())));
		children.add(new BrainGene(getEmptyChild(a.size())));
	
		for (int i=0; i<a.size(); i++) {
			if (a.get(i).size() != b.get(i).size()) {
				return createChildClones(this, partner);
			}

			for (int j=0; j<a.get(i).size(); j++) {
				List<NeuronGene> childNeurons = a.get(i).get(j).mate( b.get(i).get(j) );
				for (int k=0; k<2; k++) {
					children.get(k).layers.get(i).add(childNeurons.get(k));
				}
			}
		}
		
		return children;
	}

}
