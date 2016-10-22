package eu.luminis.robots.sim;

import eu.luminis.geometry.IBorderDimensions;
import eu.luminis.geometry.Position;
import eu.luminis.util.Range;

class BorderDimensionsPositionGenerator {
    private final IBorderDimensions borderDimensions;

    public BorderDimensionsPositionGenerator(IBorderDimensions borderDimensions) {
        this.borderDimensions = borderDimensions;
    }

    public Position createRandomPositionWithinBorder() {
        return createRandomPosition(this.borderDimensions);
    }

    public Position createRandomPositionWithinFixedBorder(int border) {
        BorderDimensions borderDimensions = new BorderDimensions(this.borderDimensions, border);
        return createRandomPosition(borderDimensions);
    }

    public Position createRandomPositionWithinRelativeBorder(double relativeSize) {
        BorderDimensions borderDimensions = new BorderDimensions(this.borderDimensions, relativeSize);
        return createRandomPosition(borderDimensions);
    }

    private Position createRandomPosition(IBorderDimensions borderDimensions) {
        Range rangeX = new Range(borderDimensions.getMinX(), borderDimensions.getMaxX());
        Range rangeY = new Range(borderDimensions.getMinY(), borderDimensions.getMaxY());

        return new Position(rangeX.random(), rangeY.random(), Math.random() * Math.PI * 2);
    }
}
