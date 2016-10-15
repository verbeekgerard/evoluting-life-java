package eu.luminis.robots;

import eu.luminis.brains.Brain;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Robot {
    private final Brain brain;
    private final IMotorsController motorsController;
    private final IServo servo;
    private final ISensor sensor;

    public Robot(Brain brain, IMotorsController motorsController, IServo servo, ISensor sensor) {
        this.brain = brain;
        this.motorsController = motorsController;
        this.servo = servo;
        this.sensor = sensor;
    }

    public void run() {
        // 1. sense
        double distance = this.sensor.sense();
        int angle = this.servo.getAngle();
        // 2. think
        // 3. control left and right motors
        // 4. turn servo
        throw new NotImplementedException();
    }

    private BrainOutput think(BrainInput input) {
        throw new NotImplementedException();
    }
}
