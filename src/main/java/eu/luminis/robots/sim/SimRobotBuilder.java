package eu.luminis.robots.sim;

import eu.luminis.Options;
import eu.luminis.brains.BrainBuilder;
import eu.luminis.brains.IBrain;
import eu.luminis.events.EventBroadcaster;
import eu.luminis.events.EventType;
import eu.luminis.genetics.Genome;
import eu.luminis.geometry.Vector;
import eu.luminis.robots.core.Robot;

/**
 * Builds a SimRobot
 */
public class SimRobotBuilder {
    private Genome genome;
    private SimWorld world;
    private Vector position;

    private SimRobotBuilder() {

    }

    public static SimRobotBuilder simRobot() {
        return new SimRobotBuilder();
    }

    public SimRobotBuilder withWorld(SimWorld world) {
        this.world = world;

        PositionGenerator positionGenerator = new PositionGenerator(world);
        position = positionGenerator.createRandomPositionWithinFixedBorder(2);

        return this;
    }

    public SimRobotBuilder withGenome(Genome genome) {
        this.genome = genome;
        return this;
    }

    public SimRobotBuilder withPosition(Vector position) {
        this.position = position;
        return this;
    }

    public SimRobot build() {
        SimMovementRecorder movementRecorder = initializeSimMovementRecorder();
        SimServoAngleRecorder angleRecorder = new SimServoAngleRecorder();
        SimCollisionRecorder collisionRecorder = initializeSimCollisionRecorder(movementRecorder);

        SimRobot newSimRobot = initializeSimRobot(movementRecorder, angleRecorder, collisionRecorder);

        EventBroadcaster.getInstance().broadcast(EventType.NEW_ROBOT, newSimRobot);

        return newSimRobot;
    }

    private SimRobot initializeSimRobot(SimMovementRecorder movementRecorder, SimServoAngleRecorder angleRecorder, SimCollisionRecorder collisionRecorder) {
        SimMotorsController motorsController = initializeMotorsController(movementRecorder);
        SimServoController servoController = initializeServoController(angleRecorder);
        SimSensorController sensorController = initializeSensorController(movementRecorder, collisionRecorder, angleRecorder);
        IBrain brain = initializeBrain(genome);

        Robot robot = new Robot(
            brain,
            motorsController,
            servoController,
            sensorController
        );

        SimLife simLife = initializeSimLife(genome);

        return new SimRobot(genome, world, robot, simLife, movementRecorder, angleRecorder, collisionRecorder, sensorController);
    }

    private SimMovementRecorder initializeSimMovementRecorder() {
        return new SimMovementRecorder(position);
    }

    private SimCollisionRecorder initializeSimCollisionRecorder(SimMovementRecorder movementRecorder) {
        return new SimCollisionRecorder(
                world,
                movementRecorder,
                Options.sizeOption.get(),
                genome.getSensor().getViewDistance());
    }

    private IBrain initializeBrain(Genome genome) {
        return BrainBuilder
                .brain()
                .withBrainChromosome(genome.getBrain())
                .build();
    }

    private SimLife initializeSimLife(Genome genome) {
        int oldAge = (int)genome.getLife().getOldAge();
        return new SimLife(oldAge);
    }

    private SimMotorsController initializeMotorsController(SimMovementRecorder movementRecorder) {
        return new SimMotorsController(movementRecorder, genome.getMovement().getLinearForce());
    }

    private SimServoController initializeServoController(SimServoAngleRecorder angleRecorder) {
        return new SimServoController(angleRecorder, genome.getSensor().getFieldOfView(), genome.getMovement().getAngularForce());
    }

    private SimSensorController initializeSensorController(SimMovementRecorder movementRecorder, SimCollisionRecorder collisionRecorder, SimServoAngleRecorder angleRecorder) {
        return new SimSensorController(collisionRecorder, movementRecorder, genome.getSensor().getFieldOfView(), angleRecorder);
    }
}
