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

    public List<Obstacle> getAllObstacles() {
        List<Obstacle> obstacles = new ArrayList<>();

        obstacles.addAll(robotPopulation.getAllRobots());
        obstacles.addAll(obstaclePopulation.getAllRoundObstacles());

        return obstacles;
    }

    public SimRobotPopulation getRobotPopulation() {
        return robotPopulation;
    }

    public StationaryObstaclePopulation getObstaclePopulation() {
        return obstaclePopulation;
    }
}
