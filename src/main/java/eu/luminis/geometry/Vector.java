package eu.luminis.geometry;

/**
 * Represents a 2d vector
 */
public abstract class Vector {
    private double x;
    private double y;
    private double angle;
    private double length;

    private boolean isPolar = false;

    public static Vector cartesian(double x, double y) {
        return new CartesianVector(x, y);
    }

    public static Vector polar(double angle, double length) {
        return new PolarVector(angle, length);
    }

    protected Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    protected Vector(double angle, double length, boolean polar) {
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
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public double getLength() {
        return length;
    }

    public double relativeAngle(Vector other) {
        return Radians.getBoundedArcTan(other.getX() - getX(), other.getY() - getY());
    }

    public Vector add(Vector vector) {
        return Vector.cartesian(getX() + vector.getX(), getY() + vector.getY());
    }

    public Vector subtract(Vector vector) {
        return Vector.cartesian(getX() - vector.getX(), getY() - vector.getY());
    }

    public double distance(Vector vector) {
        return getLength(getX() - vector.getX(), getY() - vector.getY());
    }

    protected static double getLength(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }
}
