package eu.luminis.robots.sim;

import eu.luminis.geometry.IBorderDimensions;

class BorderDimensions implements IBorderDimensions {
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    public BorderDimensions(int width, int height) {
        minX = 0;
        maxX = width;
        minY = 0;
        maxY = height;
    }

    public BorderDimensions(IBorderDimensions borderDimensions, double factor) {
        this(
                borderDimensions,
                (int) (borderDimensions.getWidth() - borderDimensions.getWidth() * factor) / 2,
                (int) (borderDimensions.getHeight() - borderDimensions.getHeight() * factor) / 2
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
