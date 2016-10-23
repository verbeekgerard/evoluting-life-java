package eu.luminis.robots.sim;

import eu.luminis.geometry.Position;

class ObstacleVector implements Comparable<ObstacleVector> {
	private final double distance;
	private final double angle;
	private final double size;
	private Position position;

	public ObstacleVector(double distance, double angle, double size, Position position) {
		this.distance = distance;
		this.angle = angle;
		this.size = size;
		this.position = position;
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

	public double getSize() {
		return size;
	}

	public Position getPosition() {
		return position;
	}
}
