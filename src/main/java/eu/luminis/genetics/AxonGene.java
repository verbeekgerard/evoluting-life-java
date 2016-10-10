package eu.luminis.genetics;

import eu.luminis.general.Options;
import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.List;

public class AxonGene extends Gene {
	private double strength;
	private double strengthening;
	private double weakening;

	public AxonGene(double strength, double strengthening, double weakening) {
		this.strength = strength;
	    this.strengthening = strengthening;
	    this.weakening = weakening;
	}
	
	public AxonGene() {
		initializeStrength();
        initializeStrengthening();
        initializeWeakening();
    }

    public double getStrength() {
        return strength;
    }

    public double getStrengthening() {
        return strengthening;
    }

    public double getWeakening() {
        return weakening;
    }

    public void mutate() {
        if (Math.random() <= Options.strengthReplacementRate.get()) {
            initializeStrength();
        }
        else if (Math.random() <= Options.strengthMutationRate.get()) {
            this.strength += new Range(-1 * Options.maxStrength.get(), Options.maxStrength.get()).mutation(Options.mutationFraction.get());
        }

        if (Math.random() <= Options.strengtheningReplacementRate.get()) {
            initializeStrengthening();
        }
        else if (Math.random() <= Options.strengtheningMutationRate.get()) {
            this.strengthening += new Range(Options.minStrengthening.get(), Options.maxStrengthening.get()).mutation(Options.mutationFraction.get());
        }

        if (Math.random() <= Options.weakeningReplacementRate.get()) {
            initializeWeakening();
        }
        else if (Math.random() <= Options.weakeningMutationRate.get()) {
            this.weakening += new Range(Options.minWeakening.get(), Options.maxWeakening.get()).mutation(Options.mutationFraction.get());
        }
    }

    public List<AxonGene> mate(AxonGene partner) {
    	return (List<AxonGene>) new Genetics().mate(this, partner);
    }

    @Override
    public List<Double> getInitiateProperties() {
        List<Double> list = new ArrayList<>();
        list.add(this.strength);
        list.add(this.strengthening);
        list.add(this.weakening);

        return list;
    }

    @Override
	public Gene initiate(List<Double> properties) {
		return new AxonGene(properties.get(0), properties.get(1), properties.get(2));
	}

    private void initializeStrength() {
        this.strength = new Range(-1 * Options.maxStrength.get(), Options.maxStrength.get()).random();
    }

    private void initializeStrengthening() {
        this.strengthening =  new Range(Options.minStrengthening.get(), Options.maxStrengthening.get()).random();
    }

    private void initializeWeakening() {
        this.weakening = new Range(Options.minWeakening.get(), Options.maxWeakening.get()).random();
    }
}