package eu.luminis.robots.sim;

import eu.luminis.genetics.LifeGene;
import eu.luminis.geometry.Vector;

/**
 * Builds up a RoundSimObstacle
 */
public class RoundSimObstacleBuilder {
    private LifeGene lifeChromosome = new LifeGene();
    private SimMovementRecorder movementRecorder;
    private SimWorld world;

    private RoundSimObstacleBuilder(){

    }

    public static RoundSimObstacleBuilder roundSimObstacle() {
        return new RoundSimObstacleBuilder();
    }

    public RoundSimObstacleBuilder withWorld(SimWorld world) {
        this.world = world;

        PositionGenerator positionGenerator = new PositionGenerator(world);
        Vector position = positionGenerator.createRandomPositionWithinFixedBorder(2);
        initializeSimMovementRecorder(position);

        return this;
    }

    public RoundSimObstacleBuilder withPosition(Vector position) {
        initializeSimMovementRecorder(position);
        return this;
    }

    public RoundSimObstacleBuilder withLifeChromosome(LifeGene lifeChromosome) {
        this.lifeChromosome = lifeChromosome;
        return this;
    }

    public RoundSimObstacle build() {
        SimLife simLife = new SimLife((int)lifeChromosome.getOldAge());
        return new RoundSimObstacle(world, movementRecorder, simLife);
    }

    private void initializeSimMovementRecorder(Vector position) {
        this.movementRecorder = new SimMovementRecorder(position);
    }

}
