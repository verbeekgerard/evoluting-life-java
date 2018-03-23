package eu.luminis.robots.pi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.RaspiPin;
import eu.luminis.Options;
import eu.luminis.robots.core.IMotorsController;
import eu.luminis.util.Physics;

public class PiMotorsController implements IMotorsController, IPiController {
    private final static GpioController gpio = Pi4JControllerFactory.GetController();

    private final Physics physics;
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

        this.physics = new Physics(Options.linearFriction.get(), linearForce);
        this.leftMotor = new PiMotor(gpio, RaspiPin.GPIO_23, RaspiPin.GPIO_25); // 12, 13
        this.rightMotor = new PiMotor(gpio, RaspiPin.GPIO_26, RaspiPin.GPIO_28); // 19, 16
    }

    @Override
    public void move(double leftChange, double rightChange) {
        velocityLeft = physics.calculateVelocity(velocityLeft, leftChange);
        velocityRight = physics.calculateVelocity(velocityRight, rightChange);

        move(velocityLeft, leftMotor);
        move(velocityRight, rightMotor);
    }

    @Override
    public void shutdown() {
        leftMotor.shutdown();
        rightMotor.shutdown();
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
