package eu.luminis.robots.sim;

import eu.luminis.Options;
import eu.luminis.robots.core.IServoController;
import eu.luminis.util.Physics;
import eu.luminis.util.Range;

class SimServoController implements IServoController {
    private final SimServoAngleRecorder angleRecorder;
    private final Physics physics;
    private final double viewAngle;
    private final Range viewAngleRange;

    private double angle;
    private double angularVelocity = 0;

    public SimServoController(SimServoAngleRecorder angleRecorder, double viewAngle, double angularForce) {
        this.physics = new Physics(Options.angularFriction.get(), angularForce);
        this.angleRecorder = angleRecorder;
        this.viewAngle = viewAngle;
        this.viewAngleRange = new Range(-1 * viewAngle/2, viewAngle/2);
    }

    @Override
    public double getAngle() {
        return this.angle;
    }

    @Override
    public void changeAngle(double change) {
        angularVelocity = physics.calculateVelocity(angularVelocity, change);
        angle = viewAngleRange.assureBounds(angle + angularVelocity);

        if (angle == viewAngleRange.getLowerBound() || angle == viewAngleRange.getUpperBound()) {
            angularVelocity = 0;
        }

        angleRecorder.recordAngleChange(angle, change * physics.getMaxForce());
    }

    @Override
    public double getViewAngle() {
        return viewAngle;
    }
}
