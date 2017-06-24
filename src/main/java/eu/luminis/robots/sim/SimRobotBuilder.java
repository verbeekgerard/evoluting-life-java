package eu.luminis.robots.sim;

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
        return this;
    }

    public SimRobotBuilder withPosition(Position position) {
        this.position = position;
        return this;
    }

    public SimRobot build() {
        SimRobot newSimRobot = new SimRobot(genome, position, world);

        EventBroadcaster.getInstance().broadcast(EventType.NEW_ROBOT, newSimRobot);

        return newSimRobot;
    }
}
