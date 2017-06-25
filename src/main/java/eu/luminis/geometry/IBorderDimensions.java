package eu.luminis.geometry;

public interface IBorderDimensions {
    int getMinX();
    int getMaxX();
    int getMinY();
    int getMaxY();

    default int getWidth() {
        return getMaxX() - getMinX();
    }

    default int getHeight() {
        return getMaxY() - getMinY();
    }

    default boolean isWithinBorders(Vector position) {
        return position.getX() > getMinX() && position.getX() < getMaxX() &&
                position.getY() > getMinY() && position.getY() < getMaxY();
    }
}
