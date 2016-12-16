package eu.luminis.robots.pi;

import java.io.IOException;

import eu.luminis.Options;
import eu.luminis.robots.core.IMotorsController;

public class PiMotorsController implements IMotorsController {
    private final static double linearFriction = Options.linearFriction.get();

    private final double linearForce;
    private PiMotor leftMotor = null;
    private PiMotor rightMotor = null;

    private double velocityLeft = 0;
    private double velocityRight = 0;

    public PiMotorsController(double linearForce) {
        try {
			this.leftMotor = new PiMotor(17, 23);
			this.rightMotor = new PiMotor(22, 27);
		} catch (IOException e) {
			// TODO: Auto-generated catch block
			e.printStackTrace();
		}
        this.linearForce = linearForce;
    }

    @Override
    public void move(double leftChange, double rightChange) {
        calculateVelocityLeft(leftChange);
        calculateVelocityRight(rightChange);

        move(velocityLeft, leftMotor);
        move(velocityRight, rightMotor);
    }

    private void calculateVelocityRight(double rightChange) {
        double accelerationRight = rightChange * linearForce;
        velocityRight += accelerationRight;
        velocityRight -= velocityRight * linearFriction;
    }

    private void calculateVelocityLeft(double leftChange) {
        double accelerationLeft = leftChange * linearForce;
        velocityLeft += accelerationLeft;
        velocityLeft -= velocityLeft * linearFriction;
    }

    private static void move(double velocity, PiMotor motor) {
        if (velocity > 0) {
            motor.forward();
        } else if (velocity < 0) {
            motor.reverse();
        } else {
            motor.stop();
        }
    }
}
