package eu.luminis.robots.sim;

import eu.luminis.entities.Position;
import eu.luminis.general.Options;
import eu.luminis.robots.core.IMotorsController;

public class SimMotorsController implements IMotorsController {
    private SimRobot owner;

    private final double linearForce;
    private double linearFriction = Options.linearFrictionOption.get();

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
        Position p = owner.getPosition();

        // Keep angles within bounds
        p.a = p.a % (Math.PI * 2);
        if (p.a < 0)
            p.a += Math.PI * 2;

        // F=m*a => a=F/m, dv=a*dt => dv=dt*F/m, dt=one cycle, m=1
        double accelerationLeft = leftChange * linearForce;
        velocityLeft += accelerationLeft;
        velocityLeft -= velocityLeft * linearFriction;

        double accelerationRight = rightChange * linearForce;
        velocityRight += accelerationRight;
        velocityRight -= velocityRight * linearFriction;

        p.a += (velocityLeft - velocityRight) / 10;

        // Convert movement vector into polar
        double dx = (Math.cos(p.a) * getVelocity());
        double dy = (Math.sin(p.a) * getVelocity());
        p.x += dx;
        p.y += dy;

        owner.recordMove(p, accelerationLeft + accelerationRight);
    }
}
