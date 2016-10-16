package eu.luminis.sensors;

import eu.luminis.robots.Obstacle;

public class ObstacleVector implements Comparable<ObstacleVector> {
	private double distance;
	private double angle;
	private Obstacle organism;

	public ObstacleVector(double distance, double angle, Obstacle organism){
		this.distance = distance;
		this.angle = angle;
		this.organism = organism;
	}

	@Override
	public int compareTo(ObstacleVector o) {
		return new Double(this.distance).compareTo(o.distance);
	}

	public double getDistance() {
		return distance;
	}

	public double getAngle() {
		return angle;
	}

	public Obstacle getOrganism() {
		return organism;
	}
}
