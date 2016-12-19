package eu.luminis.robots.pi;

import eu.luminis.Options;
import eu.luminis.robots.core.IMotorsController;

import java.io.IOException;

public class PiPwmMotorsController implements IMotorsController, IPiController {
    private final static double linearFriction = Options.linearFriction.get();

    private final double linearForce;
    private final PiPwmMotor leftMotor;
    private final PiPwmMotor rightMotor;

    private double velocityLeft = 0;
    private double velocityRight = 0;

    public PiPwmMotorsController(double linearForce) throws IOException {
        double maxVelocity = linearForce / linearFriction;
        this.leftMotor = new PiPwmMotor(12, 13, maxVelocity);
        this.rightMotor = new PiPwmMotor(19, 16, maxVelocity);
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

    private static void move(double velocity, PiPwmMotor motor) {
        if (velocity > 0) {
            motor.forward(velocity);
        } else if (velocity < 0) {
            motor.reverse(velocity);
        } else {
            motor.stop();
        }
    }

    @Override
    public void shutdown() throws IOException {
        leftMotor.shutdown();
        rightMotor.shutdown();
    }
}
