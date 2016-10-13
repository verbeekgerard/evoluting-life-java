package eu.luminis.entities;

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
		return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
	}

	@Override
	public String toString() {
		return "x: " + x + ", y: " + y + ", a: " + a;
	}
}