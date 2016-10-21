package eu.luminis.robots.sim;

import eu.luminis.geometry.Position;
import eu.luminis.general.Options;
import eu.luminis.genetics.LifeGene;
import eu.luminis.util.Range;

public class RoundSimObstacle extends SimObstacle {
    private double growthPercentage;
    private double size;

    public RoundSimObstacle(SimWorld world, Position position) {
        super(world, position, new LifeGene());

        growthPercentage = new Range(Options.minGrowthPercentage.get(), Options.maxGrowthPercentage.get()).random();
        size = new Range(Options.minRoundObstacleSize.get(), Options.maxRoundObstacleSize.get()).random();
    }

    @Override
    public double getSize() {
        return size;
    }

    @Override
    protected void run() {
        this.size += this.size * this.growthPercentage / 100;

        if (this.size > Options.maxRoundObstacleSize.get()) {
            this.size = Options.maxRoundObstacleSize.get();
        }
    }
}
