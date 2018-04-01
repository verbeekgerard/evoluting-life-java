package eu.luminis.robots.sim;

import eu.luminis.Options;
import eu.luminis.geometry.Vector;
import eu.luminis.robots.core.IMotorsController;
import eu.luminis.util.Physics;

class SimMotorsController implements IMotorsController {
    private final SimMovementRecorder movementRecorder;
    private final Physics physics;

    private double velocityLeft = 0;
    private double velocityRight = 0;

    public SimMotorsController(SimMovementRecorder movementRecorder, double linearForce) {
        this.movementRecorder = movementRecorder;
        this.physics = new Physics(Options.linearFriction.get(), linearForce);
    }

    public double getVelocity() {
        return (velocityRight + velocityLeft) / 2;
    }

    @Override
    public void move(double leftChange, double rightChange) {
        velocityLeft = physics.calculateVelocity(velocityLeft, leftChange);
        velocityRight = physics.calculateVelocity(velocityRight, rightChange);

        Vector currentVelocity = movementRecorder.getVelocity();
        double directionChange = (velocityLeft - velocityRight) / 10;
        Vector velocity = Vector.polar(currentVelocity.getAngle() + directionChange, getVelocity());

        double acceleration = (Math.abs(leftChange) + Math.abs(rightChange)) * physics.getMaxForce();
        movementRecorder.recordMove(velocity, acceleration);
    }
}
