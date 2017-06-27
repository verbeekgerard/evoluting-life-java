package eu.luminis.robots.sim;

import eu.luminis.geometry.Vector;

import java.util.ArrayList;
import java.util.List;

class SensorFilter {
    private final SimMovementRecorder movementRecorder;
    private final double viewDistanceSquared;

    public SensorFilter(SimMovementRecorder movementRecorder, SimCollisionRecorder collisionRecorder) {
        this.movementRecorder = movementRecorder;
        double v2 = Math.max(collisionRecorder.getSize(), collisionRecorder.getViewDistance()); v2 *= v2;
        this.viewDistanceSquared = v2;
    }

    public List<SimObstacle> filter(List<? extends SimObstacle> obstacles) {
        Vector robotPosition = movementRecorder.getPosition();
        List<SimObstacle> filtered = new ArrayList<>();

        for (SimObstacle simObstacle : obstacles) {
            Vector obstaclePosition = simObstacle.getPosition();
            if (obstaclePosition == robotPosition) continue;

            double d2 = robotPosition.squaredDistance(obstaclePosition);
            if (d2 > viewDistanceSquared) continue;

            filtered.add(simObstacle);
        }

        return filtered;
    }
}