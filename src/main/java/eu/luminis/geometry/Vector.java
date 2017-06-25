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
    private boolean isTransformed = false;

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

        this.isTransformed = vector.isTransformed;
        this.isPolar = vector.isPolar;
    }

    public double getX() {
        if (!isPolar || isTransformed) {
            return x;
        }

        this.x = Math.cos(angle) * length;
        this.y = Math.sin(angle) * length;
        isTransformed = true;

        return x;
    }

    public double getY() {
        if (!isPolar || isTransformed) {
            return y;
        }

        this.x = Math.cos(angle) * length;
        this.y = Math.sin(angle) * length;
        isTransformed = true;

        return y;
    }

    public double getAngle() {
        if (isPolar || isTransformed) {
            return angle;
        }

        angle = Math.atan2(y, x);
        length = Math.sqrt(x * x + y * y);
        isTransformed = true;

        return angle;
    }

    public double getLength() {
        if (isPolar || isTransformed) {
            return length;
        }

        angle = Math.atan2(y, x);
        length = Math.sqrt(x * x + y * y);
        isTransformed = true;

        return length;
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
