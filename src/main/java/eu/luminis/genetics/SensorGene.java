package eu.luminis.genetics;

import eu.luminis.Options;
import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.List;

public class SensorGene extends Gene {
    private double viewDistance;
    private double fieldOfView;

    public SensorGene(double viewDistance, double fieldOfView) {
        this.viewDistance = viewDistance;
        this.fieldOfView = fieldOfView;
    }

    public SensorGene() {
        initializeViewDistance();
        initializeFieldOfView();
    }

    public double getViewDistance() {
        return viewDistance;
    }

    public double getFieldOfView() {
        return fieldOfView;
    }

    public void mutate() {
        mutateViewDistance();
        mutateFieldOfView();
    }

    public List<SensorGene> mate(SensorGene partner) {
        return new Genetics().mate(this, partner);
    }

    @Override
    public List<Double> getInitiateProperties() {
        List<Double> list = new ArrayList<>();
        list.add(viewDistance);
        list.add(fieldOfView);

        return list;
    }

    @Override
    public Gene initiate(List<Double> properties) {
        return new SensorGene(properties.get(0), properties.get(1));
    }

    private void mutateViewDistance() {
        if (Math.random() <= Options.viewDistanceReplacementRate.get()) {
            initializeViewDistance();
            return;
        }

        if (Math.random() <= Options.viewDistanceMutationRate.get()) {
            viewDistance += new Range(Options.minViewDistance.get(), Options.maxViewDistance.get())
                    .mutation(Options.mutationFraction.get());
        }
    }

    private void mutateFieldOfView() {
        if (Math.random() <= Options.fieldOfViewReplacementRate.get()) {
            initializeFieldOfView();
            return;
        }

        if (Math.random() <= Options.fieldOfViewMutationRate.get()) {
            fieldOfView += new Range(Options.minFieldOfView.get(), Options.maxFieldOfView.get())
                    .mutation(Options.mutationFraction.get());
            fieldOfView = new Range(0, 2 * Math.PI).assureBounds(fieldOfView);
        }
    }

    private void initializeViewDistance() {
        viewDistance = new Range(Options.minViewDistance.get(), Options.maxViewDistance.get()).random();
    }

    private void initializeFieldOfView() {
        fieldOfView = new Range(Options.minFieldOfView.get(), Options.maxFieldOfView.get()).random();
    }
}