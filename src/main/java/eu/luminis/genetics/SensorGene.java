package eu.luminis.genetics;

public class SensorGene extends Evolvable {
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

    public SensorGene[] mate(SensorGene partner) {
        return evolver.mate(this, partner);
    }

    @Override
    public double[] getInitiateProperties() {
        return new double[] {
                this.viewDistance,
                this.fieldOfView
        };
    }

    @Override
    public SensorGene initiate(double[] properties) {
        return new SensorGene(properties[0], properties[1]);
    }

    @Override
    public SensorGene[] newArray(int size) {
        return new SensorGene[size];
    }
}