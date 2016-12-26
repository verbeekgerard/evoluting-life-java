package eu.luminis.genetics;

import java.util.ArrayList;
import java.util.List;

public class LifeGene extends Gene {
    private static final LifeGeneEvolver evolver = new LifeGeneEvolver();

    private double oldAge;

    public LifeGene() {
        oldAge = evolver.OldAge.getNewValue();
    }

    public LifeGene(double oldAge) {
        this.oldAge = oldAge;
    }

    public double getOldAge() {
        return oldAge;
    }

    public void mutate() {
        oldAge = evolver.OldAge.mutateValue(oldAge);
    }

    public List<LifeGene> mate(LifeGene partner) {
        return evolver.mate(this, partner);
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
}