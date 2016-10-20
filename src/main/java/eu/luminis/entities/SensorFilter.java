package eu.luminis.entities;

import eu.luminis.robots.sim.SimObstacle;

import java.util.ArrayList;
import java.util.List;

public class SensorFilter {
    private final SimObstacle owner;
    private final double distanceSquared;

    public SensorFilter(SimObstacle owner, double viewDistance) {
        this.owner = owner;

        double ds2 = Math.max(owner.getSize(), viewDistance); ds2 *= ds2;
        this.distanceSquared = ds2;
    }

    public List<SimObstacle> filter(List<Plant> plants, List<Animal> animals) {
        List<SimObstacle> filteredOrganisms = new ArrayList<>();
        filteredOrganisms.addAll(filter(plants));
        filteredOrganisms.addAll(filter(animals));

        return filteredOrganisms;
    }

    public List<SimObstacle> filter(List<? extends SimObstacle> obstacles) {
        Position ownerPosition = owner.getPosition();
        List<SimObstacle> filtered = new ArrayList<>();

        for (SimObstacle simObstacle : obstacles) {
            if (simObstacle == this.owner) continue;

            // Compute the squared x and y differences
            double dx2 = simObstacle.getPosition().x - ownerPosition.x; dx2 *= dx2;
            double dy2 = simObstacle.getPosition().y - ownerPosition.y; dy2 *= dy2;

            // If the organism is outside the visible circle, skip it
            if (dx2 + dy2 > distanceSquared) continue;

            filtered.add(simObstacle);
        }

        return filtered;
    }
}