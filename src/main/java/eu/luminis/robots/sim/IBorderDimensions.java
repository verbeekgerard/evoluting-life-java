package eu.luminis.robots.sim;

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

    default void keepWithinBorders(Position position) {
        if (position.x < 0) {
            position.x = getMaxX();
        }

        if (position.x > getMaxX()) {
            position.x = 0;
        }

        if (position.y < 0) {
            position.y = getMaxY();
        }

        if (position.y > getMaxY()) {
            position.y = 0;
        }
    }
}
