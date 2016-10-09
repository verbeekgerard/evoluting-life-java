package eu.luminis.sensors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Obstacles {
	private List<ObstacleVector> obstacleVectors = new ArrayList<>();
	private double wallDistance;

	public Obstacles(List<ObstacleVector> obstacleVectors, double wallDistance) {
        Collections.sort(obstacleVectors);

        this.obstacleVectors = obstacleVectors;
		this.wallDistance = wallDistance;
	}

    public ObstacleVector getClosestObstacleVector() {
        return obstacleVectors.size() > 0 ?
                obstacleVectors.get(0) :
                null;
    }

    public double getWallDistance() {
        return wallDistance;
    }
}