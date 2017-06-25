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

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(double angle, double length, boolean polar) {
        this.isPolar = polar;
        this.angle = angle;
        this.length = length;
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

    public Vector Add(Vector vector) {
        return new Vector(getX() + vector.getX(), this.getY() + vector.getY());
    }

    public Vector Subtract(Vector vector) {
        return new Vector(getX() - vector.getX(), this.getY() - vector.getY());
    }
}
