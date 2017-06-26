package eu.luminis.geometry;

class CartesianVector extends Vector {
    public CartesianVector(double x, double y) {
        super(x, y);
    }

    @Override
    public final double getAngle() {
        return Radians.getArcTan(getX(), getY());
    }

    @Override
    public final double getLength() {
        return getLength(getX(), getY());
    }
}
