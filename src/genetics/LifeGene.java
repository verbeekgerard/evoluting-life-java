package genetics;

import main.Option;
import util.Range;

public class LifeGene {

	public Option minOldAge = new Option(3000);
	public Option maxOldAge = new Option(4000);
	public Option minNutrition = new Option(1.0);
	public Option maxNutrition = new Option(3.0);

	public Option oldAgeMutationRate = new Option(0.03);
	public Option nutritionMutationRate = new Option(0.03);
	
	public Option mutationFraction = new Option(0.001);
	
	public double oldAge;
	public double nutrition;
	
	public LifeGene(){
        this.oldAge = new Range(minOldAge.get(), maxOldAge.get()).random();
        this.nutrition = new Range(minNutrition.get(), maxNutrition.get()).random();
	}
	
	public LifeGene(double oldAge, double nutrition){
		this.oldAge = oldAge;
	    this.nutrition = nutrition;
	}
	
	public void mutate() {

        if (Math.random() <= oldAgeMutationRate.get()) {
            this.oldAge += new Range(minOldAge.get(), maxOldAge.get()).mutation(mutationFraction.get());
        }

        if (Math.random() <= nutritionMutationRate.get()) {
            this.nutrition += new Range(minNutrition.get(), maxNutrition.get()).mutation(mutationFraction.get());
        }
    }

    public LifeGene clone() {
        return new LifeGene(this.oldAge, this.nutrition);
    }

    public LifeGene mate(LifeGene partner) {
		return this.clone();
    	
//        return genetics.mate(this, partner, function (mateState) {
//            return new Self(mateState);
//        }); // TODO
    }
	
}
