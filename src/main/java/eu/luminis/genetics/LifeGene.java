package eu.luminis.genetics;

import eu.luminis.general.Options;
import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.List;

public class LifeGene extends Gene {
	private double oldAge;

	public LifeGene() {
		initializeOldAge();
	}

	public LifeGene(double oldAge) {
		this.oldAge = oldAge;
	}

	public double getOldAge() {
		return oldAge;
	}

	public void mutate() {
        if (Math.random() <= Options.oldAgeMutationRate.get()) {
            this.oldAge += new Range(Options.minOldAge.get(), Options.maxOldAge.get()).mutation(Options.mutationFraction.get());
        }
    }

    public List<LifeGene> mate(LifeGene partner) {
    	return (List<LifeGene>) new Genetics().mate(this, partner);
    }

	@Override
	public List<Double> getInitiateProperties() {
		List<Double> list = new ArrayList<>();
		list.add(this.oldAge);

		return list;
	}

	@Override
	public Gene initiate(List<Double> properties) {
		return new LifeGene(properties.get(0));
	}

	private void initializeOldAge() {
		this.oldAge = new Range(Options.minOldAge.get(), Options.maxOldAge.get()).random();
	}
}