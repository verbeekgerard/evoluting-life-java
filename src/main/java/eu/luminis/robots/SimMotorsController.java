package eu.luminis.robots;

import eu.luminis.entities.Position;
import eu.luminis.general.Options;

public class SimMotorsController implements IMotorsController {
    private SimRobot owner;

    private double linearForce;
    private double linearFriction = Options.linearFrictionOption.get();

    private double velocityLeft = 0;
    private double velocityRight = 0;

    public SimMotorsController(SimRobot owner, double linearForce) {
        this.owner = owner;
        this.linearForce = linearForce;
    }

    public double getVelocity() {
    	if (velocityRight != 0 && velocityLeft != 0) {
    		return 1;
    	}
    	else {
    		return 0;
    	}
       // return (velocityRight + velocityLeft) / 2;
    }

    @Override
    public void move(double leftChange, double rightChange) {
        Position p = owner.getPosition();

        velocityRight = rightChange;
        velocityLeft = leftChange;
        
        // Keep angles within bounds
        p.a = p.a % (Math.PI * 2);
        if (p.a < 0)
            p.a += Math.PI * 2;

//        // F=m*a => a=F/m, dv=a*dt => dv=dt*F/m, dt=one cycle, m=1
//        double accelerationLeft = leftChange * linearForce;
//        velocityLeft += accelerationLeft;
//        velocityLeft -= velocityLeft * linearFriction;
//
//        double accelerationRight = rightChange * linearForce;
//        velocityRight += accelerationRight;
//        velocityRight -= velocityRight * linearFriction;
//
//        p.a += (velocityLeft - velocityRight) / 10;
//
//        // Convert movement vector into polar
//        double dx = (Math.cos(p.a) * getVelocity());
//        double dy = (Math.sin(p.a) * getVelocity());
//        p.x += dx;
//        p.y += dy;
        
		if (leftChange != 0 && rightChange != 0) {
			int speed = 1;
//			p.x += speed * Math.cos(p.a * Math.PI / 180);
//			p.y += speed * Math.sin(p.a * Math.PI / 180);
			p.x += speed * Math.cos(p.a);
			p.y += speed * Math.sin(p.a);
			
			//this.usedEnergy += this.costCalculator.accelerate(1);
			owner.recordMove(p, 1);
		}
		else {
			if (leftChange == 0 && rightChange != 0) {
				p.a += 0.05;
				owner.recordMove(p, 0);
			}
			else if (leftChange != 0 && rightChange == 0) {
				p.a -= 0.05;
				owner.recordMove(p, 0);
			}
			
			//this.usedEnergy += this.costCalculator.rotate(0.05);
		}


        
    }
}
