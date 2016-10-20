package eu.luminis.robots.sim;

import eu.luminis.entities.Position;
import eu.luminis.general.Options;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StationaryObstaclePopulation {
    private final SimWorld world;
    private final BorderDimensionsPositionGenerator borderDimensionsPositionGenerator;

    private List<SimObstacle> roundSimObstacles = new CopyOnWriteArrayList<>(); // Slow lost but no exceptions in UI

    public StationaryObstaclePopulation(SimWorld world) {
        this.world = world;
        this.borderDimensionsPositionGenerator = new BorderDimensionsPositionGenerator(world);

        double populationSize = (int)Options.roundObstaclePopulationSize.get();
        for (int i = 0; i < populationSize; i++) {
            spawnNewRoundSimObstacle();
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
        return borderDimensionsPositionGenerator.createRandomPositionWithinRelativeBorder(2);
    }

    private RoundSimObstacle spawnNewRoundSimObstacle() {
        Position position = createRandomPosition();
        RoundSimObstacle newSimObstacle = new RoundSimObstacle(world, position);
        roundSimObstacles.add(newSimObstacle);

        return newSimObstacle;
    }
}
