package eu.luminis.robots;

import java.util.ArrayList;
import java.util.List;

public class SimWorld {
    private SimRobotPopulation robotPopulation;
    private StationaryObstaclePopulation obstaclePopulation;
    private int width = 1280;
    private int height = 720;

    @Deprecated
    public SimWorld() {
    }

    public SimWorld(SimRobotPopulation robotPopulation, StationaryObstaclePopulation obstaclePopulation) {
        this.robotPopulation = robotPopulation;
        this.obstaclePopulation = obstaclePopulation;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
