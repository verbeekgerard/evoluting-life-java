package genetics;

import java.util.ArrayList;
import java.util.List;

import General.Options;
import util.Range;

public class LifeGene extends Gene {

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

    public List<LifeGene> mate(LifeGene partner) {
    	return (List<LifeGene>) new Genetics().mate(this, partner);
    }
    
    public List<String> getInitiateProperties() {
		List<String> properties = new ArrayList<>();
		properties.add("oldAge");
		properties.add("nutrition");
		return properties;
	}
	
	public LifeGene initiate(List<Double> properties){
		return new LifeGene(properties.get(0), properties.get(1));
	}
	
}
