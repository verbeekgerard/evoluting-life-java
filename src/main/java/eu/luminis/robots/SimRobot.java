package eu.luminis.robots;

import eu.luminis.entities.Position;
import eu.luminis.entities.World;
import eu.luminis.genetics.Genome;

public class SimRobot extends Obstacle {
    private final Genome genome;

    private SimMotor leftMotor;
    private SimMotor rightMotor;
    private SimServo servo;
    private SimSensor sensor;

    private Robot robot;

    public SimRobot(Genome genome, Position position, World world) {
        super(world, position);

        this.genome = genome;
        this.leftMotor = new SimMotor(this);
        this.rightMotor = new SimMotor(this);
        this.servo = new SimServo(this);
        this.sensor = new SimSensor(this);

        this.robot = new Robot(this.leftMotor, this.rightMotor, this.servo, this. sensor);
    }
}
