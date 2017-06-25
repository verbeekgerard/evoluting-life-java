package eu.luminis.robots.sim;

import eu.luminis.geometry.IBorderDimensions;
import eu.luminis.geometry.Vector;
import eu.luminis.util.Range;

class PositionGenerator {
    private final IBorderDimensions borderDimensions;

    public PositionGenerator(IBorderDimensions borderDimensions) {
        this.borderDimensions = borderDimensions;
    }

    public Vector createRandomPositionWithinBorder() {
        return createRandomPosition(this.borderDimensions);
    }

    public Vector createRandomPositionWithinFixedBorder(int border) {
        BorderDimensions borderDimensions = new BorderDimensions(this.borderDimensions, border);
        return createRandomPosition(borderDimensions);
    }

    public Vector createRandomPositionWithinRelativeBorder(double relativeSize) {
        BorderDimensions borderDimensions = new BorderDimensions(this.borderDimensions, relativeSize);
        return createRandomPosition(borderDimensions);
    }

    private Vector createRandomPosition(IBorderDimensions borderDimensions) {
        Range rangeX = new Range(borderDimensions.getMinX(), borderDimensions.getMaxX());
        Range rangeY = new Range(borderDimensions.getMinY(), borderDimensions.getMaxY());

        return Vector.cartesian(rangeX.random(), rangeY.random());
    }
}
