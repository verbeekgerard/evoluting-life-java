package eu.luminis.robots.sim;

import eu.luminis.geometry.Radians;
import eu.luminis.geometry.Vector;

class ObstacleVector implements Comparable<ObstacleVector> {
    private final double squaredDistance;
	private final double angle;
	private final double size;
	private Vector position;

	public ObstacleVector(Vector originPosition, Vector originVelocity, Vector obstaclePosition, double obstacleSize) {
		double globalAngle = originPosition.relativeAngle(obstaclePosition);

		angle = Radians.getRelativeDifference(originVelocity.getAngle(), globalAngle);
        squaredDistance = originPosition.squaredDistance(obstaclePosition);
		position = obstaclePosition;
        size = obstacleSize;
	}

	@Override
	public int compareTo(ObstacleVector o) {
		return new Double(this.squaredDistance).compareTo(o.squaredDistance);
	}

    public double getSquaredDistance() {
        return squaredDistance;
    }

	public double getAngle() {
		return angle;
	}

	public double getSize() {
		return size;
	}

	public Vector getPosition() {
		return position;
	}
}
