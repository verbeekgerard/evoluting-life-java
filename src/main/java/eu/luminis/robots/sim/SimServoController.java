package eu.luminis.robots.sim;

import eu.luminis.Options;
import eu.luminis.robots.core.IServoController;
import eu.luminis.util.Range;

class SimServoController implements IServoController {
    private final static double angularFriction = Options.angularFriction.get();

    private final SimRobot owner;
    private final double viewAngle;
    private final double angularForce;
    private final Range viewAngleRange;

    private double angle;
    private double angularVelocity = 0;

    public SimServoController(SimRobot owner, double viewAngle, double angularForce) {
        this.owner = owner;
        this.viewAngle = viewAngle;
        this.angularForce = angularForce;
        this.viewAngleRange = new Range(-1 * viewAngle/2, viewAngle/2);
    }

    @Override
    public double getAngle() {
        return this.angle;
    }

    @Override
    public void changeAngle(double acceleration) {
        double angularAcceleration = acceleration * angularForce;
        angularVelocity += angularAcceleration;
        angularVelocity -= angularVelocity * angularFriction;

        angle = viewAngleRange.assureBounds(angle + angularVelocity);

        if (angle == viewAngleRange.getLowerBound() || angle == viewAngleRange.getUpperBound()) {
            angularVelocity = 0;
        }

        owner.recordAngleChange(acceleration);
    }

    @Override
    public double getViewAngle() {
        return viewAngle;
    }
}
