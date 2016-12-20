package eu.luminis.robots.pi;

import java.io.IOException;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.RaspiPin;
import eu.luminis.Options;
import eu.luminis.robots.core.IMotorsController;

public class PiMotorsController implements IMotorsController, IPiController {
    private final static double linearFriction = Options.linearFriction.get();
    private final static GpioController gpio = Pi4JControllerFactory.GetController();

    private final double linearForce;
    private final PiMotor leftMotor;
    private final PiMotor rightMotor;

    private double velocityLeft = 0;
    private double velocityRight = 0;

    public PiMotorsController(double linearForce) {
        this.leftMotor = new PiMotor(gpio, RaspiPin.GPIO_26, RaspiPin.GPIO_23); // 12, 13
        this.rightMotor = new PiMotor(gpio, RaspiPin.GPIO_24, RaspiPin.GPIO_27); // 19, 16
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

    @Override
    public void shutdown() throws IOException {
        leftMotor.shutdown();
        rightMotor.shutdown();
    }
}
