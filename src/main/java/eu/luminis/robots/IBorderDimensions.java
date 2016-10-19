package eu.luminis.robots;

import eu.luminis.entities.Position;

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
}
