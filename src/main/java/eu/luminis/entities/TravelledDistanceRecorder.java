package eu.luminis.entities;

public class TravelledDistanceRecorder {

    private int steps = 0;
    private double historicalDistance = 0;
    private double currentDistance = 0;
    private Position startPosition;

    public TravelledDistanceRecorder(Position startPosition) {
        this.startPosition = new Position(startPosition);
    }

    public void recordMove(Position position) {
        this.currentDistance = position.calculateDistance(this.startPosition);

        this.steps++;
        if (steps > 300 || this.currentDistance > 300) {
            initializeDistanceVariables(position);
        }
    }

    public double getTotalDistance() {
        return this.historicalDistance + this.currentDistance;
    }

    private void initializeDistanceVariables(Position position) {
        this.historicalDistance += this.currentDistance;
        this.steps = 0;
        this.currentDistance = 0;
        this.startPosition = new Position(position);
    }
}
