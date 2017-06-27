package eu.luminis.genetics;

public class AxonGene extends Gene {
    private static final AxonGeneEvolver evolver = new AxonGeneEvolver();

    private double strength;
    private double strengthening;
    private double weakening;

    public AxonGene() {
        strength = evolver.Strength.getNewValue();
        strengthening = evolver.Strengthening.getNewValue();
        weakening = evolver.Weakening.getNewValue();
    }

    public AxonGene(double strength, double strengthening, double weakening) {
        this.strength = strength;
        this.strengthening = strengthening;
        this.weakening = weakening;
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

    public void mutate() {
        strength = evolver.Strength.mutateValue(strength);
        strengthening = evolver.Strengthening.mutateValue(strengthening);
        weakening = evolver.Weakening.mutateValue(weakening);
    }

    public AxonGene[] mate(AxonGene partner) {
        return evolver.mate(this, partner);
    }

    @Override
    public double[] getInitiateProperties() {
        return new double[] {
            this.strength,
            this.strengthening,
            this.weakening
        };
    }

    @Override
    public AxonGene initiate(double[] properties) {
        return new AxonGene(properties[0], properties[1], properties[2]);
    }

    @Override
    public AxonGene[] newArray(int size) {
        return new AxonGene[size];
    }
}