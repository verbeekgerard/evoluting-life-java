package eu.luminis.geometry;

class PolarVector extends Vector {
    public PolarVector(double angle, double length) {
        super(angle, length, true);
    }

    @Override
    public final double getX() {
        return Math.cos(getAngle()) * getLength();
    }

    @Override
    public final double getY() {
        return Math.sin(getAngle()) * getLength();
    }
}
