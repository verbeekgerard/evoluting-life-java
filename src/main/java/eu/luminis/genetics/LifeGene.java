package eu.luminis.genetics;

public class LifeGene extends Evolvable {
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

    public LifeGene[] mate(LifeGene partner) {
        return evolver.mate(this, partner);
    }

    @Override
    public double[] getInitiateProperties() {
        return new double[] {this.oldAge};
    }

    @Override
    public LifeGene initiate(double[] properties) {
        return new LifeGene(properties[0]);
    }

    @Override
    public LifeGene[] newArray(int size) {
        return new LifeGene[size];
    }
}