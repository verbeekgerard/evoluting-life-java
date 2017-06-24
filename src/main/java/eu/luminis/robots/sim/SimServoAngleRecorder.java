package eu.luminis.robots.sim;

import eu.luminis.evolution.CostCalculator;
import eu.luminis.robots.core.IAngleRetriever;

/**
 * Records changes in the angle of the servo
 */
public class SimServoAngleRecorder implements IAngleRetriever {
    private static final CostCalculator costCalculator = CostCalculator.getInstance();

    private double angle;
    private Double headTurnCost = 0.0;

    public void recordAngleChange(double angle, double acceleration) {
        this.angle = angle;
        headTurnCost += costCalculator.turnHead(acceleration);
    }

    public Double getHeadTurnCost() {
        return headTurnCost;
    }

    @Override
    public double getAngle() {
        return angle;
    }
}
