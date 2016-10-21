package eu.luminis.geometry;

public class Position {

	public double x;
	public double y;
	public double a;

	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Position(double x, double y, double a) {
		this.x = x;
		this.y = y;
		this.a = a;
	}

	public Position(Position p) {
		this.x = p.x;
		this.y = p.y;
		this.a = p.a;
	}

	public double calculateDistance(Position other) {
		return Math.sqrt(Math.pow(other.x-x, 2) + Math.pow(other.y-y, 2));
	}

    public double calculateAngle(Position other) {
        return Radians.getBoundedArcTan(other.x-x, other.y-y);
    }

    public double getBoundedAngle() {
		return Radians.getBounded(a);
	}

    public Position getRelativeDifference(Position other) {
        return new Position(other.x-x, other.y-y, Radians.getBoundedDifference(a, other.a));
    }

    @Override
	public String toString() {
		return "x: " + x + ", y: " + y + ", a: " + a;
	}
}