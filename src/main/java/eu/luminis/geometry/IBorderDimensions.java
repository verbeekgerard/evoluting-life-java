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

    default boolean isWithinBorders(Position position) {
        return position.x > getMinX() && position.x < getMaxX() &&
                position.y > getMinY() && position.y < getMaxY();
    }

    default void keepWithinBorders(Position position) {
        if (position.x < getMinX()) position.x = getMinX();
        if (position.x > getMaxX()) position.x = getMaxX();
        if (position.y < getMinY()) position.y = getMinY();
        if (position.y > getMaxY()) position.y = getMaxY();
    }
}
