package eu.luminis.robots.sim;

import eu.luminis.entities.Position;
import eu.luminis.util.Range;

public class BorderDimensionsPositionGenerator {
    private IBorderDimensions borderDimensions;

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

    private class BorderDimensions implements IBorderDimensions {
        private int minX;
        private int maxX;
        private int minY;
        private int maxY;

        public BorderDimensions(IBorderDimensions borderDimensions, double factor) {
            this(
                    borderDimensions,
                    (int)(borderDimensions.getWidth() - borderDimensions.getWidth() * factor) / 2,
                    (int)(borderDimensions.getHeight() - borderDimensions.getHeight() * factor) / 2
            );
        }

        public BorderDimensions(IBorderDimensions borderDimensions, int border) {
            this(borderDimensions, border, border);
        }

        public BorderDimensions(IBorderDimensions borderDimensions, int borderX, int borderY) {
            minX = borderX;
            maxX = borderDimensions.getMaxX() - borderX;
            minY = borderY;
            maxY = borderDimensions.getMaxY() - borderY;
        }

        @Override
        public int getMinX() {
            return minX;
        }

        @Override
        public int getMaxX() {
            return maxX;
        }

        @Override
        public int getMinY() {
            return minY;
        }

        @Override
        public int getMaxY() {
            return maxY;
        }
    }
}
