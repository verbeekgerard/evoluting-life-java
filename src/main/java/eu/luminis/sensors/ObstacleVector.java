package eu.luminis.sensors;

public class ObstacleVector implements Comparable<ObstacleVector> {
	private double distance;
	private double angle;

	public ObstacleVector(double distance, double angle){
		this.distance = distance;
		this.angle = angle;
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
}
