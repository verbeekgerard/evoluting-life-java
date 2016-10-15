package eu.luminis.robots;

import java.util.ArrayList;
import java.util.List;

public class SimWorld {
    private SimRobotPopulation robotPopulation;
    private StationaryObstaclePopulation obstaclePopulation;

    public SimWorld(SimRobotPopulation robotPopulation, StationaryObstaclePopulation obstaclePopulation) {
        this.robotPopulation = robotPopulation;
        this.obstaclePopulation = obstaclePopulation;
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
