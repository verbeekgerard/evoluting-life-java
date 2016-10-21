package eu.luminis.robots.sim;

public class ObstacleVector implements Comparable<ObstacleVector> {
	private double distance;
	private double angle;
	private double size;

	public ObstacleVector(double distance, double angle, double size){
		this.distance = distance;
		this.angle = angle;
		this.size = size;
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
}
