package eu.luminis.robots.sim;

import eu.luminis.Options;
import eu.luminis.robots.core.IServoController;
import eu.luminis.util.Range;

class SimServoController implements IServoController {
    private final static double angularFriction = Options.angularFriction.get();

    private final SimServoAngleRecorder angleRecorder;
    private final double viewAngle;
    private final double angularForce;
    private final Range viewAngleRange;

    private double angle;
    private double angularVelocity = 0;

    public SimServoController(SimServoAngleRecorder angleRecorder, double viewAngle, double angularForce) {
        this.angleRecorder = angleRecorder;
        this.viewAngle = viewAngle;
        this.angularForce = angularForce;
        this.viewAngleRange = new Range(-1 * viewAngle/2, viewAngle/2);
    }

    @Override
    public double getAngle() {
        return this.angle;
    }

    @Override
    public void changeAngle(double change) {
        angularVelocity = calculateVelocity(angularVelocity, change);
        angle = viewAngleRange.assureBounds(angle + angularVelocity);

        if (angle == viewAngleRange.getLowerBound() || angle == viewAngleRange.getUpperBound()) {
            angularVelocity = 0;
        }

        angleRecorder.recordAngleChange(angle, change * angularForce);
    }

    @Override
    public double getViewAngle() {
        return viewAngle;
    }

    private double calculateVelocity(double initialVelocity, double change) {
        return initialVelocity * (1 - initialVelocity * angularFriction) + change * angularForce;
    }
}
