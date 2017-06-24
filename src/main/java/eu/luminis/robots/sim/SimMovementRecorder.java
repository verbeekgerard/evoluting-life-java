package eu.luminis.robots.sim;

import eu.luminis.evolution.CostCalculator;
import eu.luminis.geometry.Position;

/**
 * Records the actions of the motors
 */
public class SimMovementRecorder {
    private static final CostCalculator costCalculator = CostCalculator.getInstance();

    private final TravelledDistanceRecorder distanceRecorder;
    private final Position position;
    private Double movementCost = 0.0;

    public SimMovementRecorder(Position position) {
        this.position = position;
        this.distanceRecorder = new TravelledDistanceRecorder(position);
    }

    public Position getPosition() {
        return position;
    }

    public void recordMove(Position newPosition, double acceleration) {
        distanceRecorder.recordMove(newPosition);
        movementCost += costCalculator.accelerate(acceleration);
    }

    public Double getMovementCost() {
        return movementCost;
    }

    public Double getTotalDistance() {
        return distanceRecorder.getTotalDistance();
    }
}
