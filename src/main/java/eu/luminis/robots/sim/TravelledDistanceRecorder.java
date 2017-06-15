package eu.luminis.robots.sim;

import eu.luminis.geometry.Position;

class TravelledDistanceRecorder {
    private int steps = 0;
    private double historicalDistance = 0;
    private double currentDistance = 0;
    private Position startPosition;

    public TravelledDistanceRecorder(Position startPosition) {
        initializeDistanceVariables(startPosition);
    }

    public void recordMove(Position position) {
        currentDistance = position.calculateDistance(startPosition);

        steps++;
        if (steps > 500 || currentDistance > 250) {
            initializeDistanceVariables(position);
        }
    }

    public Double getTotalDistance() {
        return historicalDistance + currentDistance;
    }

    private void initializeDistanceVariables(Position position) {
        historicalDistance += currentDistance;
        steps = 0;
        currentDistance = 0;
        startPosition = new Position(position);
    }
}