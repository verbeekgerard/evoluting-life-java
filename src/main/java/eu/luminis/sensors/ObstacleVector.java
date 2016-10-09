package eu.luminis.sensors;

import eu.luminis.entities.Organism;

public class ObstacleVector implements Comparable<ObstacleVector> {
	private double distance;
	private double angle;
	private Organism organism;

	public ObstacleVector(double distance, double angle, Organism organism){
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

	public Organism getOrganism() {
		return organism;
	}
}
