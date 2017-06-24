package eu.luminis.geometry;

/**
 * A vector that represents a velocity
 */
public class Velocity {
    private final double magnitude;
    private final double angle;

    public Velocity(double magnitude, double angle) {
        this.magnitude = magnitude;
        this.angle = angle;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public double getAngle() {
        return angle;
    }

    public double getX() {
        return Math.cos(angle) * magnitude;
    }

    public double getY() {
        return Math.sin(angle) * magnitude;
    }
}
