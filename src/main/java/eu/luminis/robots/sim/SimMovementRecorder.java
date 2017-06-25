package eu.luminis.robots.sim;

import eu.luminis.evolution.CostCalculator;
import eu.luminis.geometry.Position;
import eu.luminis.geometry.Velocity;

/**
 * Records the actions of the motors
 */
public class SimMovementRecorder {
    private static final CostCalculator costCalculator = CostCalculator.getInstance();

    private final TravelledDistanceRecorder distanceRecorder;
    private final Position position;
    private Velocity velocity;
    private Double movementCost = 0.0;

    public SimMovementRecorder(Position position) {
        this.position = position;
        this.velocity = new Velocity(0, position.a);
        this.distanceRecorder = new TravelledDistanceRecorder(position);
    }

    public Position getPosition() {
        return position;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public void recordMove(Velocity newVelocity, double acceleration) {
        this.velocity = newVelocity;
        this.position.Add(newVelocity);
        this.distanceRecorder.recordMove(this.position);

        this.movementCost += costCalculator.accelerate(acceleration);
    }

    public Double getMovementCost() {
        return movementCost;
    }

    public Double getTotalDistance() {
        return distanceRecorder.getTotalDistance();
    }
}