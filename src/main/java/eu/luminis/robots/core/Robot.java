package eu.luminis.robots.core;

import eu.luminis.brains.Brain;

import java.util.List;

public class Robot {
    private final Brain brain;
    private final IMotorsController motorsController;
    private final IServoController servoController;
    private final ISensorController sensorController;

    private double previousServoAngle;

    public Robot(Brain brain, IMotorsController motorsController, IServoController servoController, ISensorController sensorController) {
        this.brain = brain;
        this.motorsController = motorsController;
        this.servoController = servoController;
        this.sensorController = sensorController;

        this.previousServoAngle = servoController.getAngle();
    }

    public void run() {
        // 1. sense
        double distance = sensorController.sense();
        double servoAngle = servoController.getAngle();

        // 2. think
        BrainInput input = new BrainInput(distance, sensorController.getViewDistance(), servoAngle, servoController.getViewAngle(), servoAngle - previousServoAngle);
        List<Double> output = brain.think(input.getValues());
        BrainOutput brainOutput = new BrainOutput(output);

        // 3. control left and right motors
        motorsController.move(brainOutput.getMotorAccelerationLeft(), brainOutput.getMotorAccelerationRight());

        // 4. control the servo motor
        servoController.changeAngle(brainOutput.getServoAcceleration());
    }
}
