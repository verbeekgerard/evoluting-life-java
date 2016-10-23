package eu.luminis.robots.sim;

import eu.luminis.geometry.Position;
import eu.luminis.general.Options;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StationaryObstaclePopulation {
    private final SimWorld world;
    private final PositionGenerator positionGenerator;

    private final List<SimObstacle> roundSimObstacles = new CopyOnWriteArrayList<>(); // Slow lost but no exceptions in UI

    public StationaryObstaclePopulation(SimWorld world) {
        this.world = world;
        this.positionGenerator = new PositionGenerator(world);

        double populationSize = (int)Options.roundObstaclePopulationSize.get();
        for (int i = 0; i < populationSize; i++) {
            RoundSimObstacle newSimObstacle = spawnNewRoundSimObstacle();
            roundSimObstacles.add(newSimObstacle);
        }
    }

    public void run(){
        for (int i=0; i < roundSimObstacles.size(); i++) {
            SimObstacle roundSimObstacle = roundSimObstacles.get(i);

            if (!roundSimObstacle.survives()) {
                roundSimObstacle = roundSimObstacles.set(i, spawnNewRoundSimObstacle());
            }

            roundSimObstacle.runCycle();
        }
    }

    public List<SimObstacle> getAllRoundObstacles() {
        return roundSimObstacles;
    }

    private Position createRandomPosition() {
        return positionGenerator.createRandomPositionWithinFixedBorder(2);
    }

    private RoundSimObstacle spawnNewRoundSimObstacle() {
        Position position = createRandomPosition();
        return new RoundSimObstacle(world, position);
    }
}
