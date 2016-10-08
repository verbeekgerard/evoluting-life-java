package eu.luminis.genetics;

import eu.luminis.general.Options;
import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.List;

public class NeuronGene extends Gene {
	private List<AxonGene> axons = new ArrayList<>();
	private double threshold;
	private double relaxation;
	
	public NeuronGene(int maxOutputs) {
        this.threshold = new Range(Options.minThreshold.get(), Options.maxThreshold.get()).random();
        this.relaxation = Math.floor(new Range(0, Options.maxRelaxation.get()).random()) / 100;

        for (int i=0; i<maxOutputs; i++) {
            this.axons.add(new AxonGene());
        }
	}
	
	public NeuronGene(double threshold, double relaxation) {
        this.threshold = threshold;
        this.relaxation = relaxation;
	}
	
	public NeuronGene(double threshold, double relaxation, List<AxonGene> axons) {

        this.threshold = threshold;
        this.relaxation = relaxation;

        for (int i=0; i< axons.size(); i++) {
        	AxonGene axon = axons.get(i);
            this.axons.add(new AxonGene(axon.getStrength(), axon.getStrengthening(), axon.getWeakening()));
        }
	}
    
    public List<NeuronGene> mate(NeuronGene partner) {
    	
    	List<NeuronGene> children = (List<NeuronGene>) new Genetics().mate(this, partner);
    	
        for (int i=0; i<this.axons.size(); i++) {
            List<AxonGene> childAxons = this.axons.get(i).mate(partner.axons.get(i));
            
            for (int j=0; j<children.size(); j++) {
            	children.get(j).axons.add( childAxons.get(j) );
            }
        }

    	return children;
    }

    @Override
    public List<Double> getInitiateProperties() {
        List<Double> list = new ArrayList<>();
        list.add(this.threshold);
        list.add(this.relaxation);

        return list;
    }

    @Override
    public Gene initiate(List<Double> properties) {
        return new NeuronGene(properties.get(0), properties.get(1));
    }

    public void mutate() {
        Range range;

        if (Math.random() < Options.thresholdReplacementRate.get()) {
            this.threshold = new Range(Options.minThreshold.get(), Options.maxThreshold.get()).random();
        }
        else if (Math.random() < Options.thresholdMutationRate.get()) {
            range = new Range(Options.minThreshold.get(), Options.maxThreshold.get());
            this.threshold += range.mutation(Options.mutationFraction.get());
            this.threshold = range.checkLower(this.threshold);
        }

        if (Math.random() < Options.relaxationReplacementRate.get()) {
            this.relaxation = Math.floor(new Range(0, Options.maxRelaxation.get()).random()) / 100;
        }
        else if (Math.random() < Options.relaxationMutationRate.get()) {
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

    public List<AxonGene> getAxons() {
        return axons;
    }

    public double getThreshold() {
        return threshold;
    }

    public double getRelaxation() {
        return relaxation;
    }
}