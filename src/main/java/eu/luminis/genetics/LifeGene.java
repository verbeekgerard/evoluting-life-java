package eu.luminis.genetics;

import eu.luminis.general.Options;
import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.List;

public class LifeGene extends Gene {
	private double oldAge;
	private double nutrition;
	
	public LifeGene() {
        this.oldAge = new Range(Options.minOldAge.get(), Options.maxOldAge.get()).random();
        this.nutrition = new Range(Options.minNutrition.get(), Options.maxNutrition.get()).random();
	}
	
	public LifeGene(double oldAge, double nutrition) {
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

	@Override
	public List<Double> getInitiateProperties() {
		List<Double> list = new ArrayList<>();
		list.add(this.oldAge);
		list.add(this.nutrition);

		return list;
	}

	@Override
	public Gene initiate(List<Double> properties) {
		return new LifeGene(properties.get(0), properties.get(1));
	}

	public double getOldAge() {
		return oldAge;
	}

	public double getNutrition() {
		return nutrition;
	}
}