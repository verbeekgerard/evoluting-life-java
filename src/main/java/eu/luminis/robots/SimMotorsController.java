package eu.luminis.robots;

import eu.luminis.entities.Position;
import eu.luminis.entities.TravelledDistanceRecorder;
import eu.luminis.general.CostCalculator;
import eu.luminis.general.Options;

public class SimMotorsController implements IMotorsController {
    private CostCalculator costCalculator = CostCalculator.getInstance();
    private SimRobot owner;
    private TravelledDistanceRecorder distanceRecorder;

    private double linearForce;
    private double linearFriction = Options.linearFrictionOption.get();
    private double movementCost = 0;

    private double velocityLeft = 0;
    private double velocityRight = 0;

    public SimMotorsController(SimRobot owner) {
        this.owner = owner;
        this.linearForce = owner.getGenome().getMovement().getLinearForce();
        this.distanceRecorder = new TravelledDistanceRecorder(owner.getPosition());
    }

    public double getMovementCost() {
        return movementCost;
    }

    public double getTravelledDistanceReward() {
        return costCalculator.distanceReward(distanceRecorder.getTotalDistance());
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

        distanceRecorder.recordMove(p);
        movementCost += costCalculator.accelerate(accelerationLeft + accelerationRight);
    }

    private double getVelocity() {
        return (velocityRight + velocityLeft) / 2;
    }
}
