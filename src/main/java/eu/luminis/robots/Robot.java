package eu.luminis.robots;

import eu.luminis.brains.Brain;

import java.util.List;

public class Robot {
    private final Brain brain;
    private final IMotorsController motorsController;
    private final IServoController servoController;
    private final ISensorController sensorController;

    public Robot(Brain brain, IMotorsController motorsController, IServoController servoController, ISensorController sensorController) {
        this.brain = brain;
        this.motorsController = motorsController;
        this.servoController = servoController;
        this.sensorController = sensorController;
    }

    public void run() {
        // 1. sense
        double distance = sensorController.sense();
        int angle = servoController.getAngle();

        // 2. think
        BrainInput input = new BrainInput(distance, angle, Math.PI);
        List<Double> output = brain.think(input.getValues());
        BrainOutput brainOutput = new BrainOutput(output);

        // 3. control left and right motors
        motorsController.move(brainOutput.getMotorAccelerationLeft(), brainOutput.getMotorAccelerationRight());

        // 4. control the servo motor
        servoController.changeAngle(brainOutput.getServoAcceleration());
    }
}
