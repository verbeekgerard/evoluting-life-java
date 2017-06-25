package eu.luminis.robots.sim;

import eu.luminis.Options;
import eu.luminis.geometry.Velocity;
import eu.luminis.robots.core.IMotorsController;

class SimMotorsController implements IMotorsController {
    private final static double linearFriction = Options.linearFriction.get();

    private final SimMovementRecorder movementRecorder;
    private final double linearForce;

    private double velocityLeft = 0;
    private double velocityRight = 0;

    public SimMotorsController(SimMovementRecorder movementRecorder, double linearForce) {
        this.movementRecorder = movementRecorder;
        this.linearForce = linearForce;
    }

    public double getVelocity() {
        return (velocityRight + velocityLeft) / 2;
    }

    @Override
    public void move(double leftChange, double rightChange) {
        velocityLeft = calculateVelocity(velocityLeft, leftChange);
        velocityRight = calculateVelocity(velocityRight, rightChange);

        Velocity currentVelocity = movementRecorder.getVelocity();
        double directionChange = (velocityLeft - velocityRight) / 10;
        Velocity velocity = new Velocity(currentVelocity.getAngle() + directionChange, getVelocity());

        double acceleration = (Math.abs(leftChange) + Math.abs(rightChange)) * linearForce;
        movementRecorder.recordMove(velocity, acceleration);
    }

    private double calculateVelocity(double initialVelocity, double linearChange) {
        // F=m*a => a=F/m, dv=a*dt => dv=dt*F/m, dt=one cycle, m=1
        return initialVelocity * (1 - initialVelocity * linearFriction) + linearChange * linearForce;
    }
}
