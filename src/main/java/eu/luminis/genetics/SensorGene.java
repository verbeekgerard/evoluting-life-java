package eu.luminis.genetics;

import java.util.ArrayList;
import java.util.List;

public class SensorGene extends Gene {
    private static final SensorGeneEvolver evolver = new SensorGeneEvolver();

    private double viewDistance;
    private double fieldOfView;

    public SensorGene() {
        viewDistance = evolver.ViewDistance.getNewValue();
        fieldOfView = evolver.FieldOfView.getNewValue();
    }

    public SensorGene(double viewDistance, double fieldOfView) {
        this.viewDistance = viewDistance;
        this.fieldOfView = fieldOfView;
    }

    public double getViewDistance() {
        return viewDistance;
    }

    public double getFieldOfView() {
        return fieldOfView;
    }

    public void mutate() {
        viewDistance = evolver.ViewDistance.mutateValueWithLowerBound(viewDistance, 0);
        fieldOfView = evolver.FieldOfView.mutateValueWithBounds(fieldOfView, 0, 2 * Math.PI);
    }

    public List<SensorGene> mate(SensorGene partner) {
        return evolver.mate(this, partner);
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
}