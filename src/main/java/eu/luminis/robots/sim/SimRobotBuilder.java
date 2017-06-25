package eu.luminis.robots.sim;

import eu.luminis.brains.BrainBuilder;
import eu.luminis.brains.IBrain;
import eu.luminis.events.EventBroadcaster;
import eu.luminis.events.EventType;
import eu.luminis.genetics.Genome;
import eu.luminis.geometry.Vector;

/**
 * Builds a SimRobot
 */
public class SimRobotBuilder {
    private Genome genome;
    private SimMovementRecorder movementRecorder;
    private SimWorld world;

    private IBrain brain;
    private SimLife simLife;

    private SimRobotBuilder() {

    }

    public static SimRobotBuilder simRobot() {
        return new SimRobotBuilder();
    }

    public SimRobotBuilder withWorld(SimWorld world) {
        this.world = world;

        PositionGenerator positionGenerator = new PositionGenerator(world);
        Vector position = positionGenerator.createRandomPositionWithinFixedBorder(2);
        initializeSimMovementRecorder(position);

        return this;
    }

    public SimRobotBuilder withGenome(Genome genome) {
        this.genome = genome;
        this.brain = initializeBrain(genome);
        this.simLife = initializeSimLife(genome);

        return this;
    }

    public SimRobotBuilder withPosition(Vector position) {
        initializeSimMovementRecorder(position);
        return this;
    }

    public SimRobot build() {
        SimServoAngleRecorder angleRecorder = new SimServoAngleRecorder();
        SimRobot newSimRobot = new SimRobot(genome, world, brain, simLife, movementRecorder, angleRecorder);

        EventBroadcaster.getInstance().broadcast(EventType.NEW_ROBOT, newSimRobot);

        return newSimRobot;
    }

    private void initializeSimMovementRecorder(Vector position) {
        this.movementRecorder = new SimMovementRecorder(position);
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
}
