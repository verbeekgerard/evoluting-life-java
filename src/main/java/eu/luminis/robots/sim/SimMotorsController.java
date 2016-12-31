package eu.luminis.robots.sim;

import eu.luminis.geometry.Position;
import eu.luminis.Options;
import eu.luminis.robots.core.IMotorsController;

class SimMotorsController implements IMotorsController {
    private final static double linearFriction = Options.linearFriction.get();

    private final SimRobot owner;
    private final double linearForce;

    private double velocityLeft = 0;
    private double velocityRight = 0;

    public SimMotorsController(SimRobot owner, double linearForce) {
        this.owner = owner;
        this.linearForce = linearForce;
    }

    public double getVelocity() {
        return (velocityRight + velocityLeft) / 2;
    }

    @Override
    public void move(double leftChange, double rightChange) {
        velocityLeft = calculateVelocity(velocityLeft, leftChange);
        velocityRight = calculateVelocity(velocityRight, rightChange);

        Position ownerPosition = owner.getPosition();
        ownerPosition.a += (velocityLeft - velocityRight) / 10;

        // Convert movement vector into polar
        double dx = (Math.cos(ownerPosition.a) * getVelocity());
        double dy = (Math.sin(ownerPosition.a) * getVelocity());
        ownerPosition.x += dx;
        ownerPosition.y += dy;

        double acceleration = (Math.abs(leftChange) + Math.abs(rightChange)) * linearForce;
        owner.recordMove(ownerPosition, acceleration);
    }

    private double calculateVelocity(double initialVelocity, double linearChange) {
        // F=m*a => a=F/m, dv=a*dt => dv=dt*F/m, dt=one cycle, m=1
        return initialVelocity * (1 - initialVelocity * linearFriction) + linearChange * linearForce;
    }
}
