package eu.luminis.robots.pi;

import eu.luminis.Options;
import eu.luminis.robots.core.IServoController;
import eu.luminis.util.Range;

public class PiServoController implements IServoController {
    private final static double angularFriction = Options.angularFriction.get();
    private static final double deg_0 = 0.58;
    private static final double deg_90 = 1.36;
    private static final double deg_180 = 2.32;

    private final PiServo piServo = new PiServo(0, deg_0, deg_90, deg_180, 2.33, 100);

    private final double angularForce;
    private final Range viewAngleRange;

    private final double viewAngle = Math.PI;

    private Double angle;
    private double angularVelocity = 0;

    public PiServoController(double angularForce) {
        this.angularForce = angularForce;
        this.viewAngleRange = new Range(-1 * viewAngle/2, viewAngle/2);

        initializeStartAngle();
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

        int degrees = (int)Math.toDegrees(angle) + 90;

        piServo.moveTo(degrees);
    }

    @Override
    public double getViewAngle() {
        return viewAngle;
    }

    private void initializeStartAngle() {
        int startAngle = 90;

        angle = (double) startAngle;
        piServo.moveTo(startAngle);
    }
}
