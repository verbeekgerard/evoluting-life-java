package eu.luminis.genetics;

import eu.luminis.Options;
import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NeuronGene extends Gene {
    private double threshold;
    private double relaxation;
    private final List<AxonGene> axons = new ArrayList<>();

    public NeuronGene(int maxOutputs) {
        initializeThreshold();
        initializeRelaxation();

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

    public List<NeuronGene> mate(NeuronGene partner) {
        List<NeuronGene> children = new Genetics().mate(this, partner);

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

    public void mutate() {
        mutateThreshold();
        mutateRelaxation();
        mutateAxons();
    }

    private void mutateThreshold() {
        if (Math.random() < Options.thresholdReplacementRate.get()) {
            initializeThreshold();
            return;
        }

        if (Math.random() < Options.thresholdMutationRate.get()) {
            Range range = new Range(Options.minThreshold.get(), Options.maxThreshold.get());
            this.threshold += range.mutation(Options.mutationFraction.get());
            this.threshold = range.assureLowerBound(this.threshold);
        }
    }

    private void mutateRelaxation() {
        if (Math.random() < Options.relaxationReplacementRate.get()) {
            initializeRelaxation();
            return;
        }

        if (Math.random() < Options.relaxationMutationRate.get()) {
            Range range = new Range(0, Options.maxRelaxation.get());
            this.relaxation += Math.floor(range.mutation(Options.mutationFraction.get())) / 100;
            this.relaxation = range.assureBounds(this.relaxation);
        }
    }

    private void mutateAxons() {
        for (int i = 0; i < this.axons.size(); i++) {
            this.axons.get(i).mutate();
        }
    }

    private void initializeThreshold() {
        this.threshold = new Range(Options.minThreshold.get(), Options.maxThreshold.get()).random();
    }

    private void initializeRelaxation() {
        this.relaxation = Math.floor(new Range(0, Options.maxRelaxation.get()).random()) / 100;
    }
}