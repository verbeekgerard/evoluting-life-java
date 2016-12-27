package eu.luminis.genetics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NeuronGene extends Gene {
    private static final NeuronGeneEvolver evolver = new NeuronGeneEvolver();

    private double threshold;
    private double relaxation;
    private final List<AxonGene> axons = new ArrayList<>();

    public NeuronGene(int maxOutputs) {
        threshold = evolver.Threshold.getNewValue();
        relaxation = evolver.Relaxation.getNewValue();

        for (int i = 0; i < maxOutputs; i++) {
            this.axons.add(new AxonGene());
        }
    }

    public NeuronGene(double threshold, double relaxation) {
        this.threshold = threshold;
        this.relaxation = relaxation;
    }

    public NeuronGene(double threshold, double relaxation, List<AxonGene> axons) {
        this.threshold = threshold;
        this.relaxation = relaxation;

        this.axons.addAll(axons.stream()
                .map(axon -> new AxonGene(axon.getStrength(), axon.getStrengthening(), axon.getWeakening()))
                .collect(Collectors.toList()));
    }

    public double getThreshold() {
        return threshold;
    }

    public double getRelaxation() {
        return relaxation;
    }

    public List<AxonGene> getAxons() {
        return axons;
    }

    public void mutate() {
        threshold = evolver.Threshold.mutateValueWithLowerBound(threshold);
        relaxation = evolver.Relaxation.mutateValueWithBounds(relaxation);
        mutateAxons();
    }

    public List<NeuronGene> mate(NeuronGene partner) {
        List<NeuronGene> children = evolver.mate(this, partner);

        for (int i = 0; i < this.axons.size(); i++) {
            List<AxonGene> childAxons = this.axons.get(i).mate(partner.axons.get(i));

            for (int j = 0; j < children.size(); j++) {
                children.get(j).axons.add(childAxons.get(j));
            }
        }

        return children;
    }

    @Override
    public List<Double> getInitiateProperties() {
        List<Double> list = new ArrayList<>();
        list.add(this.threshold);
        list.add(this.relaxation);

        return list;
    }

    @Override
    public Gene initiate(List<Double> properties) {
        return new NeuronGene(properties.get(0), properties.get(1));
    }

    private void mutateAxons() {
        for (int i = 0; i < this.axons.size(); i++) {
            this.axons.get(i).mutate();
        }
    }
}