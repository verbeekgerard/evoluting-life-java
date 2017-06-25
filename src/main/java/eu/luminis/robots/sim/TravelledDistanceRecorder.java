package eu.luminis.robots.sim;

import eu.luminis.Options;
import eu.luminis.geometry.Position;

class TravelledDistanceRecorder {
    private int steps = 0;
    private double historicalDistance = 0;
    private double currentDistance = 0;
    private Position startPosition;

    private static final double travelledDistanceSavePointSteps = Options.travelledDistanceSavePointSteps.get();
    private static final double travelledDistanceSavePointDistance = Options.travelledDistanceSavePointDistance.get();

    public TravelledDistanceRecorder(Position startPosition) {
        initializeDistanceVariables(startPosition);
    }

    public void recordMove(Position position) {
        currentDistance = position.distance(startPosition);

        steps++;
        if (steps > travelledDistanceSavePointSteps || currentDistance > travelledDistanceSavePointDistance) {
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