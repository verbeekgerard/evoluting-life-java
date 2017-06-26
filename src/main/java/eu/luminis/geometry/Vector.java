package eu.luminis.geometry;

/**
 * Represents a 2d vector
 */
public class Vector {
    private Double x;
    private Double y;
    private Double angle;
    private Double length;

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
        if (x == null) {
            x = Math.cos(angle) * length;
        }

        return x;
    }

    public double getY() {
        if (y == null) {
            y = Math.sin(angle) * length;
        }

        return y;
    }

    public double getAngle() {
        if (angle == null) {
            angle = Radians.getArcTan(x, y);
        }

        return angle;
    }

    public double getLength() {
        if (length == null) {
            length = getLength(x, y);
        }

        return length;
    }

    public double relativeAngle(Vector other) {
        return Radians.getBoundedArcTan(other.getX() - getX(), other.getY() - getY());
    }

    public Vector add(Vector vector) {
        return new Vector(getX() + vector.getX(), getY() + vector.getY());
    }

    public Vector subtract(Vector vector) {
        return new Vector(getX() - vector.getX(), getY() - vector.getY());
    }

    public double distance(Vector vector) {
        return getLength(getX() - vector.getX(), getY() - vector.getY());
    }

    private static double getLength(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }
}
