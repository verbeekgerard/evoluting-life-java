package genetics;

import main.Option;
import main.Options;
import util.Range;

public class LifeGene {

	
	
	public double oldAge;
	public double nutrition;
	
	public LifeGene(){
        this.oldAge = new Range(Options.minOldAge.get(), Options.maxOldAge.get()).random();
        this.nutrition = new Range(Options.minNutrition.get(), Options.maxNutrition.get()).random();
	}
	
	public LifeGene(double oldAge, double nutrition){
		this.oldAge = oldAge;
	    this.nutrition = nutrition;
	}
	
	public void mutate() {

        if (Math.random() <= Options.oldAgeMutationRate.get()) {
            this.oldAge += new Range(Options.minOldAge.get(), Options.maxOldAge.get()).mutation(Options.mutationFraction.get());
        }

        if (Math.random() <= Options.nutritionMutationRate.get()) {
            this.nutrition += new Range(Options.minNutrition.get(), Options.maxNutrition.get()).mutation(Options.mutationFraction.get());
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
