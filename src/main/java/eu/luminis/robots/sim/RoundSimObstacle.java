package eu.luminis.robots.sim;

import eu.luminis.geometry.Position;
import eu.luminis.Options;
import eu.luminis.genetics.LifeGene;
import eu.luminis.util.Range;

class RoundSimObstacle extends SimObstacle {
    private final double growthPercentage;
    private final Range sizeRange;

    private double size;

    public RoundSimObstacle(SimWorld world, SimMovementRecorder movementRecorder, SimLife life) {
        super(world, movementRecorder, life);

        growthPercentage = new Range(Options.minGrowthPercentage.get(), Options.maxGrowthPercentage.get()).random();
        sizeRange = new Range(Options.minRoundObstacleSize.get(), Options.maxRoundObstacleSize.get());
        size = sizeRange.random();
    }

    @Override
    public double getSize() {
        return size;
    }

    @Override
    protected void run() {
        size = sizeRange.assureUpperBound(size * (1 + growthPercentage));
    }
}
