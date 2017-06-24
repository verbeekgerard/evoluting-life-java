package eu.luminis.robots.sim;

import eu.luminis.brains.BrainBuilder;
import eu.luminis.brains.IBrain;
import eu.luminis.events.EventBroadcaster;
import eu.luminis.events.EventType;
import eu.luminis.genetics.Genome;
import eu.luminis.geometry.Position;

/**
 * Builds a SimRobot
 */
public class SimRobotBuilder {
    private Genome genome;
    private Position position;
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
        this.position = positionGenerator.createRandomPositionWithinFixedBorder(2);

        return this;
    }

    public SimRobotBuilder withGenome(Genome genome) {
        this.genome = genome;
        this.brain = initializeBrain(genome);
        this.simLife = initializeSimLife(genome);

        return this;
    }

    public SimRobotBuilder withPosition(Position position) {
        this.position = position;
        return this;
    }

    public SimRobot build() {
        SimRobot newSimRobot = new SimRobot(genome, position, world, brain, simLife);

        EventBroadcaster.getInstance().broadcast(EventType.NEW_ROBOT, newSimRobot);

        return newSimRobot;
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
