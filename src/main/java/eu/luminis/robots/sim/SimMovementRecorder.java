package eu.luminis.robots.sim;

import eu.luminis.evolution.CostCalculator;
import eu.luminis.geometry.Vector;

/**
 * Records the actions of the motors
 */
public class SimMovementRecorder {
    private static final CostCalculator costCalculator = CostCalculator.getInstance();

    private final TravelledDistanceRecorder distanceRecorder;
    private Vector position;
    private Vector velocity;
    private Double movementCost = 0.0;

    public SimMovementRecorder(Vector position) {
        this(position, Vector.polar(Math.random() * Math.PI * 2, 0));
    }

    public SimMovementRecorder(Vector position, Vector velocity) {
        this.position = position;
        this.velocity = velocity;
        this.distanceRecorder = new TravelledDistanceRecorder(position);
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void recordMove(Vector newVelocity, double acceleration) {
        this.velocity = newVelocity;
        this.position = this.position.add(newVelocity);
        this.distanceRecorder.recordMove(this.position);

        this.movementCost += costCalculator.accelerate(acceleration);
    }

    public void preventOverlap() {
        this.position = position.subtract(velocity); // Move the entity opposite to it's velocity
    }

    public Double getMovementCost() {
        return movementCost;
    }

    public Double getTotalDistance() {
        return distanceRecorder.getTotalDistance();
    }
}
