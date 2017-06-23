package eu.luminis.robots.sim;

import eu.luminis.genetics.LifeGene;
import eu.luminis.geometry.Position;

/**
 * Builds up a RoundSimObstacle
 */
public class RoundSimObstacleBuilder {
    private LifeGene lifeChromosome = new LifeGene();
    private Position position;
    private SimWorld world;

    private RoundSimObstacleBuilder(){

    }

    public static RoundSimObstacleBuilder roundSimObstacle() {
        return new RoundSimObstacleBuilder();
    }

    public RoundSimObstacleBuilder withWorld(SimWorld world) {
        this.world = world;

        PositionGenerator positionGenerator = new PositionGenerator(world);
        this.position = positionGenerator.createRandomPositionWithinFixedBorder(2);

        return this;
    }

    public RoundSimObstacleBuilder withPosition(Position position) {
        this.position = position;
        return this;
    }

    public RoundSimObstacleBuilder withLifeChromosome(LifeGene lifeChromosome) {
        this.lifeChromosome = lifeChromosome;
        return this;
    }

    public RoundSimObstacle build() {
        SimLife simLife = new SimLife((int)lifeChromosome.getOldAge());
        return new RoundSimObstacle(world, position, simLife);
    }
}
