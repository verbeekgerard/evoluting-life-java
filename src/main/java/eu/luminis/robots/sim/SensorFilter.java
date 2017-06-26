package eu.luminis.robots.sim;

import eu.luminis.geometry.Vector;

import java.util.ArrayList;
import java.util.List;

class SensorFilter {
    private final SimObstacle owner;
    private final double viewDistanceSquared;

    public SensorFilter(SimObstacle owner, double viewDistance) {
        this.owner = owner;

        double v2 = Math.max(owner.getSize(), viewDistance); v2 *= v2;
        this.viewDistanceSquared = v2;
    }

    public List<SimObstacle> filter(List<? extends SimObstacle> obstacles) {
        Vector ownerPosition = owner.getPosition();
        List<SimObstacle> filtered = new ArrayList<>();

        for (SimObstacle simObstacle : obstacles) {
            if (simObstacle == owner) continue;

            double d2 = ownerPosition.squaredDistance(simObstacle.getPosition());
            if (d2 > viewDistanceSquared) continue;

            filtered.add(simObstacle);
        }

        return filtered;
    }
}