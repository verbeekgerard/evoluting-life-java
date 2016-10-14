package eu.luminis.robots;

import eu.luminis.brains.Brain;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Robot {
    private final IMotor leftMotor;
    private final IMotor rightMotor;
    private final IServo servo;
    private final ISensor sensor;

    private Brain brain;

    public Robot(IMotor leftMotor, IMotor rightMotor, IServo servo, ISensor sensor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.servo = servo;
        this.sensor = sensor;
    }

    public void run() {
        throw new NotImplementedException();
        // 1. sense
        // 2. think
        // 3. control motors
        // 4. turn servo
    }

    private BrainOuput think(BrainInput input) {
        throw new NotImplementedException();
    }
}
