package genetics;

import main.Option;
import util.Range;

public class AxonGene {

	public Option maxStrength = new Option(0.20);
	public Option minStrengthening = new Option(0.000001);
	public Option maxStrengthening = new Option(0.00002);
	public Option minWeakening = new Option(0.000001);
	public Option maxWeakening = new Option(0.000005);
	public Option strengthMutationRate = new Option(0.1);
	public Option strengtheningMutationRate = new Option(0.05);
	public Option weakeningMutationRate = new Option(0.05);
	public Option mutationFraction = new Option(0.001);
	
	public double strength;
	public double strengthening;
	public double weakening;

	public AxonGene(double strength, double strengthening, double weakening){
		this.strength = strength;
	    this.strengthening = strengthening;
	    this.weakening = weakening;
	}
	
	public AxonGene(){
		this.strength = new Range(0, maxStrength.get()).random();
        this.strengthening = new Range(minStrengthening.get(), maxStrengthening.get()).random();
        this.weakening = new Range(minWeakening.get(), maxWeakening.get()).random();
	}

    public void resetOptions() {
      this.maxStrength.reset();

      this.minStrengthening.reset();
      this.maxStrengthening.reset();
      this.minWeakening.reset();
      this.maxWeakening.reset();

      this.strengthMutationRate.reset();
      this.strengtheningMutationRate.reset();
      this.weakeningMutationRate.reset();
    }
    
    public void mutate() {

        if (Math.random() <= strengthMutationRate.get()) {
          this.strength += new Range(0, maxStrength.get()).mutation(mutationFraction.get());
        }

        if (Math.random() <= strengtheningMutationRate.get()) {
          this.strengthening += new Range(minStrengthening.get(), maxStrengthening.get()).mutation(mutationFraction.get());
        }

        if (Math.random() <= weakeningMutationRate.get()) {
          this.weakening += new Range(minWeakening.get(), maxWeakening.get()).mutation(mutationFraction.get());
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
