package eu.luminis.robots.sim;

import eu.luminis.geometry.Position;

import java.util.ArrayList;
import java.util.List;

class SensorFilter {
    private final SimObstacle owner;
    private final double viewDistance;

    public SensorFilter(SimObstacle owner, double viewDistance) {
        this.owner = owner;
        this.viewDistance = Math.max(owner.getSize(), viewDistance);
    }

    public List<SimObstacle> filter(List<? extends SimObstacle> obstacles) {
        Position ownerPosition = owner.getPosition();
        List<SimObstacle> filtered = new ArrayList<>();

        for (SimObstacle simObstacle : obstacles) {
            if (simObstacle == owner) continue;

            double distance = ownerPosition.distance(simObstacle.getPosition());
            if (distance > viewDistance) continue;

            filtered.add(simObstacle);
        }

        return filtered;
    }
}