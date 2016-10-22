package eu.luminis.robots.sim;

import eu.luminis.general.Options;
import eu.luminis.robots.core.IServoController;
import eu.luminis.util.Range;

class SimServoController implements IServoController {
    private final static Range viewAngleRange = new Range(-1 * Math.PI/2, Math.PI/2);
    private final static double angularFriction = Options.angularFriction.get();

    private final SimRobot owner;
    private double angle;
    private final double angularForce;
    private double angularVelocity = 0;

    public SimServoController(SimRobot owner, double angularForce) {
        this.owner = owner;
        this.angularForce = angularForce;
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

        owner.recordAngleChange(acceleration);
    }
}
