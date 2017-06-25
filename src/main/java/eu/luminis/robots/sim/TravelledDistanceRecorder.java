package eu.luminis.robots.sim;

import eu.luminis.Options;
import eu.luminis.geometry.Vector;

class TravelledDistanceRecorder {
    private int steps = 0;
    private double historicalDistance = 0;
    private double currentDistance = 0;
    private Vector startPosition;

    private static final double travelledDistanceSavePointSteps = Options.travelledDistanceSavePointSteps.get();
    private static final double travelledDistanceSavePointDistance = Options.travelledDistanceSavePointDistance.get();

    public TravelledDistanceRecorder(Vector startPosition) {
        initializeDistanceVariables(startPosition);
    }

    public void recordMove(Vector position) {
        currentDistance = position.distance(startPosition);

        steps++;
        if (steps > travelledDistanceSavePointSteps || currentDistance > travelledDistanceSavePointDistance) {
            initializeDistanceVariables(position);
        }
    }

    public Double getTotalDistance() {
        return historicalDistance + currentDistance;
    }

    private void initializeDistanceVariables(Vector position) {
        historicalDistance += currentDistance;
        steps = 0;
        currentDistance = 0;
        startPosition = new Vector(position);
    }
}