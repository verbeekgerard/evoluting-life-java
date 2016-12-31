package eu.luminis.robots.pi;

import eu.luminis.Options;
import eu.luminis.robots.core.IServoController;
import eu.luminis.util.Range;

public class PiServoController implements IServoController, IPiController {
    private final static double angularFriction = Options.angularFriction.get();

    private final PiServo piServo = new PiServo(0);

    private final double viewAngle;
    private final double angularForce;
    private final Range viewAngleRange;

    private double angle;
    private double angularVelocity = 0;

    public PiServoController(double viewAngle, double angularForce) {
        this.viewAngle = viewAngle;
        this.angularForce = angularForce;
        this.viewAngleRange = new Range(-1 * viewAngle/2, viewAngle/2);

        initializeStartAngle();
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

        int degrees = 90 - (int)Math.toDegrees(angle);

        piServo.moveTo(degrees);
    }

    @Override
    public double getViewAngle() {
        return viewAngle;
    }

    @Override
    public void shutdown() {
        piServo.shutdown();
    }

    private double calculateVelocity(double initialVelocity, double change) {
        return initialVelocity * (1 - initialVelocity * angularFriction) + change * angularForce;
    }

    private void initializeStartAngle() {
        int startAngle = 90;

        angle = (double) startAngle;
        piServo.moveTo(startAngle);
    }
}
