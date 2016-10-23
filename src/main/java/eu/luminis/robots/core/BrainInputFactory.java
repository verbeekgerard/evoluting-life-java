package eu.luminis.robots.core;

public class BrainInputFactory implements IBrainInputParameters {
    private final double viewDistance;
    private final double fieldOfView;

    public BrainInputFactory(double viewDistance, double fieldOfView) {
        this.viewDistance = viewDistance;
        this.fieldOfView = fieldOfView;
    }

    public BrainInput create(double distance, double angle, double previousServoAngle) {
        return new BrainInput(this, distance, angle, angle - previousServoAngle);
    }

    @Override
    public double getFieldOfView() {
        return fieldOfView;
    }

    @Override
    public double getViewDistance() {
        return viewDistance;
    }
}
