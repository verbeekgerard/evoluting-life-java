package genetics;

import main.Option;
import main.Options;
import util.Range;

public class AxonGene {

	
	
	public double strength;
	public double strengthening;
	public double weakening;

	public AxonGene(double strength, double strengthening, double weakening){
		this.strength = strength;
	    this.strengthening = strengthening;
	    this.weakening = weakening;
	}
	
	public AxonGene(){
		this.strength = new Range(0, Options.maxStrength.get()).random();
        this.strengthening = new Range(Options.minStrengthening.get(), Options.maxStrengthening.get()).random();
        this.weakening = new Range(Options.minWeakening.get(), Options.maxWeakening.get()).random();
	}
    
    public void mutate() {

        if (Math.random() <= Options.strengthMutationRate.get()) {
          this.strength += new Range(0, Options.maxStrength.get()).mutation(Options.mutationFraction.get());
        }

        if (Math.random() <= Options.strengtheningMutationRate.get()) {
          this.strengthening += new Range(Options.minStrengthening.get(), Options.maxStrengthening.get()).mutation(Options.mutationFraction.get());
        }

        if (Math.random() <= Options.weakeningMutationRate.get()) {
          this.weakening += new Range(Options.minWeakening.get(), Options.maxWeakening.get()).mutation(Options.mutationFraction.get());
        }
    }

    public AxonGene clone() {
        return new AxonGene(strength, strengthening, weakening);
    }

    public AxonGene mate(AxonGene partner) {
//        return genetics.mate(this, partner, function(child) {
//          return new Self(child);
//        });
    	return this.clone();
    }
	
	
}
