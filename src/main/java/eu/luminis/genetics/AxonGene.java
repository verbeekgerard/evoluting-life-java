package eu.luminis.genetics;

import java.util.ArrayList;
import java.util.List;

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

    public List<AxonGene> mate(AxonGene partner) {
        return evolver.mate(this, partner);
    }

    @Override
    public List<Double> getInitiateProperties() {
        List<Double> list = new ArrayList<>();
        list.add(this.strength);
        list.add(this.strengthening);
        list.add(this.weakening);

        return list;
    }

    @Override
    public Gene initiate(List<Double> properties) {
        return new AxonGene(properties.get(0), properties.get(1), properties.get(2));
    }
}