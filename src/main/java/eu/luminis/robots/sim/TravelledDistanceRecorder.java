package eu.luminis.robots.sim;

import eu.luminis.geometry.Position;

public class TravelledDistanceRecorder {
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
        if (steps > 300 || currentDistance > 300) {
            initializeDistanceVariables(position);
        }
    }

    public double getTotalDistance() {
        return historicalDistance + currentDistance;
    }

    private void initializeDistanceVariables(Position position) {
        historicalDistance += currentDistance;
        steps = 0;
        currentDistance = 0;
        startPosition = new Position(position);
    }
}