package genetics;

import java.util.ArrayList;
import java.util.List;
import util.Range;
import brains.*;
import main.Option;

public class NeuronGene {

	public List<AxonGene> axons = new ArrayList<>();
	public double threshold;
	public double relaxation;
	
	// Options
	public Option minThreshold = new Option(0);
	public Option maxThreshold = new Option(1.5);
	public Option maxRelaxation = new Option(99.0);
	public Option thresholdMutationRate = new Option(0.1);
	public Option relaxationMutationRate = new Option(0.1);
	public Option axonGeneReplacementRate = new Option(0.01);
	
	public Option mutationFraction = new Option(0.001);

    public void reset() {
      this.minThreshold.reset();
      this.maxThreshold.reset();
      this.maxRelaxation.reset();

      this.thresholdMutationRate.reset();
      this.relaxationMutationRate.reset();

      this.axonGeneReplacementRate.reset();
    }
	
	public NeuronGene(int maxOutputs){
		
        this.threshold = new Range(minThreshold.get(), maxThreshold.get()).random();
        this.relaxation = Math.floor(new Range(0, maxRelaxation.get()).random()) / 100;

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
	
//	public void getState() {
//        var axons = [];
//        for (var i=0; i<this.axons.length; i++) {
//            axons.push(this.axons[i].getState());
//        }
//
//        return {
//            threshold: this.threshold,
//            relaxation: this.relaxation,
//            axons: axons
//        };
//    };

//    public void getMateState() {
//        var state = this.getState();
//        delete state.axons;
//
//        return state;
//    };
    
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

        if (Math.random() < thresholdMutationRate.get()) {
            range = new Range(minThreshold.get(), maxThreshold.get());
            this.threshold += range.mutation(mutationFraction.get());
            this.threshold = range.checkLower(this.threshold);
        }

        if (Math.random() < relaxationMutationRate.get()) {
            range = new Range(0, maxRelaxation.get());
            this.relaxation += Math.floor(range.mutation(mutationFraction.get())) / 100;
            this.relaxation = range.check(this.relaxation);
        }

        for (int i=0; i<this.axons.size(); i++) {
            if (Math.random() < axonGeneReplacementRate.get()) {
                this.axons.set(i, new AxonGene());
                continue;
            }

            this.axons.get(i).mutate();
        }
    }
	
}
