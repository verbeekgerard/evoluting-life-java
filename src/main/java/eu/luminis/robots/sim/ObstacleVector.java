package eu.luminis.robots.sim;

import eu.luminis.geometry.Position;
import eu.luminis.geometry.Radians;
import eu.luminis.geometry.Velocity;

class ObstacleVector implements Comparable<ObstacleVector> {
	private final double distance;
	private final double angle;
	private final double size;
	private Position position;

	public ObstacleVector(Position originPosition, Velocity originVelocity, Position obstaclePosition, double obstacleSize) {
		double globalAngle = originPosition.calculateAngle(obstaclePosition);

		angle = Radians.getRelativeDifference(originVelocity.getAngle(), globalAngle);
		distance = originPosition.calculateDistance(obstaclePosition);
		position = obstaclePosition;
        size = obstacleSize;
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
