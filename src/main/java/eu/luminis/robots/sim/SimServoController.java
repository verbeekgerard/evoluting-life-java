package eu.luminis.robots.sim;

import eu.luminis.general.Options;
import eu.luminis.robots.core.IServoController;

public class SimServoController implements IServoController {
    private SimRobot owner;
    private double angle;

    private final double angularForce;
    private double angularFriction = Options.linearFriction.get();
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

        angle += angularVelocity * 1;

        if (angle < -1 * Math.PI/2) {
            angle = -1 * Math.PI/2;
        }

        if (angle > Math.PI/2) {
            angle = Math.PI/2;
        }

        owner.recordAngleChange(acceleration);
    }
}
