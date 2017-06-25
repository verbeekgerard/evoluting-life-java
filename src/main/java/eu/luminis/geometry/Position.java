package eu.luminis.geometry;

public class Position {
	private double x;
	private double y;

	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Position(Position p) {
	    this(p.x, p.y);
	}

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

	public double distance(Position other) {
		return Math.sqrt(squaredDistance(other));
	}

    public double squaredDistance(Position other) {
        return Math.pow(other.x-x, 2) + Math.pow(other.y-y, 2);
    }

    public double angle(Position other) {
        return Radians.getBoundedArcTan(other.x-x, other.y-y);
    }

    public void Add(Velocity velocity) {
	    x += velocity.getX();
	    y += velocity.getY();
    }

    public void Subtract(Velocity velocity) {
        x -= velocity.getX();
        y -= velocity.getY();
    }

    @Override
	public String toString() {
		return "x: " + x + ", y: " + y;
	}
}