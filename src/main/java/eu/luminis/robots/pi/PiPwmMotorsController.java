package eu.luminis.robots.pi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.RaspiPin;
import eu.luminis.Options;
import eu.luminis.robots.core.IMotorsController;

public class PiPwmMotorsController implements IMotorsController, IPiController {
    private final static double linearFriction = Options.linearFriction.get();
    private final static GpioController gpio = Pi4JControllerFactory.GetController();

    private final double linearForce;
    private final double maxVelocity;
    private final PiPwmMotor leftMotor;
    private final PiPwmMotor rightMotor;

    private double velocityLeft = 0.0;
    private double velocityRight = 0.0;

    public PiPwmMotorsController(double linearForce) {
        /*
            Groen   - links vooruit     - IN2 - 23 hard-pwm
            Blauw   - links achteruit   - IN1 - 25 soft-pwm
            Oranje  - rechts vooruit    - IN4 - 26 hard-pwm
            Geel    - rechts achteruit  - IN3 - 28 soft-pwm
         */

        this.leftMotor = new PiPwmMotor(gpio, RaspiPin.GPIO_23, RaspiPin.GPIO_25); // 12, 13
        this.rightMotor = new PiPwmMotor(gpio, RaspiPin.GPIO_26, RaspiPin.GPIO_28); // 19, 16
        this.linearForce = linearForce;
        this.maxVelocity = linearForce / linearFriction;
    }

    @Override
    public void move(double leftChange, double rightChange) {
        velocityLeft = calculateVelocity(velocityLeft, leftChange);
        velocityRight = calculateVelocity(velocityRight, rightChange);

        double velocityScale = calculateVelocityScale();
        
        move(velocityLeft * velocityScale, leftMotor);
        move(velocityRight * velocityScale, rightMotor);
    }

    @Override
    public void shutdown() {
        leftMotor.shutdown();
        rightMotor.shutdown();
    }

    private double calculateVelocity(double initialVelocity, double change) {
        return initialVelocity * (1 - linearFriction) + change * linearForce;
    }

    private void move(double velocity, PiPwmMotor motor) {
        if (velocity > 0.0) {
            motor.forward(velocity / maxVelocity);
        } else if (velocity < 0.0) {
            motor.reverse(-1 * velocity / maxVelocity);
        } else {
            motor.stop();
        }
    }

    private double calculateVelocityScale()
    {
        double max = Math.max(Math.abs(velocityLeft), Math.abs(velocityRight));

        return max == 0.0 ?
                0.0 :
                this.maxVelocity / max;
    }
}
