package eu.luminis.geometry;

/**
 * Represents a 2d vector
 */
public class Vector {
    private double x;
    private double y;
    private double angle;
    private double length;

    private boolean isPolar = false;

    public static Vector cartesian(double x, double y) {
        return new Vector(x, y);
    }

    public static Vector polar(double angle, double length) {
        return new Vector(angle, length, true);
    }

    private Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    private Vector(double angle, double length, boolean polar) {
        this.isPolar = polar;
        this.angle = angle;
        this.length = length;
    }

    public Vector(Vector vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.angle = vector.angle;
        this.length = vector.length;

        this.isPolar = vector.isPolar;
    }

    public double getX() {
        return isPolar ? Math.cos(angle) * length : x;
    }

    public double getY() {
        return isPolar ? Math.sin(angle) * length : y;
    }

    public double getAngle() {
        return isPolar ? angle : Math.atan2(y, x);
    }

    public double getLength() {
        return isPolar ? length : Math.sqrt(x * x + y * y);
    }

    public double relativeAngle(Vector other) {
        return Radians.getBoundedArcTan(other.getX()-getX(), other.getY()-getY());
    }

    public Vector add(Vector vector) {
        return new Vector(getX() + vector.getX(), this.getY() + vector.getY());
    }

    public Vector subtract(Vector vector) {
        return new Vector(getX() - vector.getX(), this.getY() - vector.getY());
    }

    public double distance(Vector vector) {
        return subtract(vector).getLength();
    }
}
