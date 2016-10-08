package eu.luminis.genetics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.luminis.general.Options;
import eu.luminis.util.Range;

public class AxonGene extends Gene {
	private double strength;
	private double strengthening;
	private double weakening;

	public AxonGene(double strength, double strengthening, double weakening){
		this.strength = strength;
	    this.strengthening = strengthening;
	    this.weakening = weakening;
	}
	
	public AxonGene(){
		this.strength = new Range(-1 * Options.maxStrength.get(), Options.maxStrength.get()).random();
        this.strengthening = new Range(Options.minStrengthening.get(), Options.maxStrengthening.get()).random();
        this.weakening = new Range(Options.minWeakening.get(), Options.maxWeakening.get()).random();
	}
    
    public void mutate() {

        if (Math.random() <= Options.strengthMutationRate.get()) {
          this.strength += new Range(-1 * Options.maxStrength.get(), Options.maxStrength.get()).mutation(Options.mutationFraction.get());
        }

        if (Math.random() <= Options.strengtheningMutationRate.get()) {
          this.strengthening += new Range(Options.minStrengthening.get(), Options.maxStrengthening.get()).mutation(Options.mutationFraction.get());
        }

        if (Math.random() <= Options.weakeningMutationRate.get()) {
          this.weakening += new Range(Options.minWeakening.get(), Options.maxWeakening.get()).mutation(Options.mutationFraction.get());
        }
    }

    public List<AxonGene> mate(AxonGene partner) {
    	return (List<AxonGene>) new Genetics().mate(this, partner);
    }

    @Override
    public Map<String, Double> getInitiateProperties() {
        Map<String, Double> map = new HashMap<>();
        map.put("strength", this.strength);
        map.put("strengthening", this.strengthening);
        map.put("weakening", this.weakening);

        return map;
    }

    @Override
	public Gene initiate(Map<String, Double> properties) {
		return new AxonGene(properties.get("strength"), properties.get("strengthening"), properties.get("weakening"));
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
}