package genetics;

import java.util.ArrayList;
import java.util.List;

import main.Options;
import util.Range;

public class NeuronGene extends Gene {

	public List<AxonGene> axons = new ArrayList<>();
	public double threshold;
	public double relaxation;
	
	public NeuronGene(int maxOutputs){
		
        this.threshold = new Range(Options.minThreshold.get(), Options.maxThreshold.get()).random();
        this.relaxation = Math.floor(new Range(0, Options.maxRelaxation.get()).random()) / 100;

        for (int i=0; i<maxOutputs; i++) {
            this.axons.add(new AxonGene());
        }
	}
	
	public NeuronGene(double threshold, double relaxation, List<AxonGene> axons){

        this.threshold = threshold;
        this.relaxation = relaxation;

        for (int i=0; i< axons.size(); i++) {
        	AxonGene axon = axons.get(i);
            this.axons.add(new AxonGene(axon.strength, axon.strengthening, axon.weakening));
        }
	}
    
    public List<NeuronGene> mate(NeuronGene partner) {
//        var children = genetics.mate(this, partner, function (child) {
//            child.axons = [];
//            return new Self(child);
//        });
//
//        for (int i=0; i<this.axons.length; i++) {
//            var childAxons = this.axons[i].mate(partner.axons[i]);
//
//            for (int j=0; j<children.length; j++) {
//                children[j].axons.push( childAxons[j] );
//            }
//        }
//
//        return children; // TODO
    	List<NeuronGene> children = new ArrayList<>();
    	children.add(this.clone());
    	children.add(this.clone());
    	return children;
    }
    
    public NeuronGene clone() {
        return new NeuronGene(this.threshold, this.relaxation, this.axons);
    }
    
    public void mutate() {
        Range range;

        if (Math.random() < Options.thresholdMutationRate.get()) {
            range = new Range(Options.minThreshold.get(), Options.maxThreshold.get());
            this.threshold += range.mutation(Options.mutationFraction.get());
            this.threshold = range.checkLower(this.threshold);
        }

        if (Math.random() < Options.relaxationMutationRate.get()) {
            range = new Range(0, Options.maxRelaxation.get());
            this.relaxation += Math.floor(range.mutation(Options.mutationFraction.get())) / 100;
            this.relaxation = range.check(this.relaxation);
        }

        for (int i=0; i<this.axons.size(); i++) {
            if (Math.random() < Options.axonGeneReplacementRate.get()) {
                this.axons.set(i, new AxonGene());
                continue;
            }

            this.axons.get(i).mutate();
        }
    }
	
}
