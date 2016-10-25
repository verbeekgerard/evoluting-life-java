package eu.luminis.robots.pi;

import eu.luminis.Options;
import eu.luminis.robots.core.IServoController;
import eu.luminis.util.Range;

public class PiServoController implements IServoController {
	
    private Double angle;
    private double angularVelocity = 0;
    
    private PiServo piServo;
    private int startAngle = 90;
    private final double angularForce;
    
    private final static double angularFriction = Options.angularFriction.get();
    private final Range viewAngleRange;
    private double viewAngle = 180;
    
    double deg_0 = 0.58;
    double deg_90 = 1.36;
    double deg_180 = 2.32;
    
    public PiServoController(double angularForce) {
    	piServo = new PiServo(0, deg_0, deg_90, deg_180, 2.33, 100);
        this.angularForce = angularForce;
        this.viewAngleRange = new Range(-1 * viewAngle/2, viewAngle/2);
    	// Initialize the angle
    	angle = (double) startAngle;
    	piServo.moveTo(startAngle);
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
        
        piServo.moveTo(angle.intValue());
    }

    @Override
    public double getViewAngle() {
        return viewAngle;
    }
}
