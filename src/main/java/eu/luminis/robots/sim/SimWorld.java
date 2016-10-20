package eu.luminis.robots.sim;

import eu.luminis.robots.core.IBorderDimensions;

import java.util.ArrayList;
import java.util.List;

public class SimWorld implements IBorderDimensions {
    private SimRobotPopulation robotPopulation;
    private StationaryObstaclePopulation obstaclePopulation;

    @Deprecated
    public SimWorld() {
    }

    public SimWorld(SimRobotPopulation robotPopulation, StationaryObstaclePopulation obstaclePopulation) {
        this.robotPopulation = robotPopulation;
        this.obstaclePopulation = obstaclePopulation;
    }

    @Override
    public int getMinX() {
        return 0;
    }

    @Override
    public int getMaxX() {
        return 1280;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public int getMaxY() {
        return 720;
    }

    public List<SimObstacle> getAllObstacles() {
        List<SimObstacle> simObstacles = new ArrayList<>();

        simObstacles.addAll(robotPopulation.getAllRobots());
        simObstacles.addAll(obstaclePopulation.getAllRoundObstacles());

        return simObstacles;
    }

    public SimRobotPopulation getRobotPopulation() {
        return robotPopulation;
    }

    public StationaryObstaclePopulation getObstaclePopulation() {
        return obstaclePopulation;
    }
}
