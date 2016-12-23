package eu.luminis.robots.pi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.RaspiPin;
import eu.luminis.Options;
import eu.luminis.robots.core.IMotorsController;

public class PiMotorsController implements IMotorsController, IPiController {
    private final static double linearFriction = Options.linearFriction.get() / 2.0;
    private final static GpioController gpio = Pi4JControllerFactory.GetController();

    private final double linearForce;
    private final PiMotor leftMotor;
    private final PiMotor rightMotor;

    private double velocityLeft = 0;
    private double velocityRight = 0;

    public PiMotorsController(double linearForce) {
        /*
            Groen   - links vooruit     - IN2 - 23
            Blauw   - links achteruit   - IN1 - 25
            Oranje  - rechts vooruit    - IN4 - 26
            Geel    - rechts achteruit  - IN3 - 28
         */

        this.leftMotor = new PiMotor(gpio, RaspiPin.GPIO_23, RaspiPin.GPIO_25); // 12, 13
        this.rightMotor = new PiMotor(gpio, RaspiPin.GPIO_26, RaspiPin.GPIO_28); // 19, 16
        this.linearForce = linearForce;
    }

    @Override
    public void move(double leftChange, double rightChange) {
        calculateVelocityLeft(leftChange);
        calculateVelocityRight(rightChange);

        move(velocityLeft, leftMotor);
        move(velocityRight, rightMotor);
    }

    @Override
    public void shutdown() {
        leftMotor.shutdown();
        rightMotor.shutdown();
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
