package eu.luminis.geometry;

public class Position {

	public double x;
	public double y;

	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Position(Position p) {
		this.x = p.x;
		this.y = p.y;
	}

	public double calculateDistance(Position other) {
		return Math.sqrt(calculateSquaredDistance(other));
	}

    public double calculateSquaredDistance(Position other) {
        return Math.pow(other.x-x, 2) + Math.pow(other.y-y, 2);
    }

    public double calculateAngle(Position other) {
        return Radians.getBoundedArcTan(other.x-x, other.y-y);
    }

    public void Add(Velocity velocity) {
	    x += velocity.getX();
	    y += velocity.getY();
    }

    @Override
	public String toString() {
		return "x: " + x + ", y: " + y;
	}
}