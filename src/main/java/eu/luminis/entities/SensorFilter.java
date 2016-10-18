package eu.luminis.entities;

import eu.luminis.robots.Obstacle;

import java.util.ArrayList;
import java.util.List;

public class SensorFilter {
    private final Obstacle owner;
    private final double distanceSquared;

    public SensorFilter(Obstacle owner, double viewDistance) {
        this.owner = owner;

        double ds2 = Math.max(owner.getSize(), viewDistance); ds2 *= ds2;
        this.distanceSquared = ds2;
    }

//    public List<Obstacle> filter(List<Plant> plants, List<Animal> animals) {
//        List<Obstacle> filteredOrganisms = new ArrayList<>();
//        filteredOrganisms.addAll(filter(plants));
//        filteredOrganisms.addAll(filter(animals));
//
//        return filteredOrganisms;
//    }

    public List<Obstacle> filter(List<? extends Obstacle> obstacles) {
        Position ownerPosition = owner.getPosition();
        List<Obstacle> filtered = new ArrayList<>();

        for (Obstacle obstacle : obstacles) {
            if (obstacle == this.owner) continue;

            // Compute the squared x and y differences
            double dx2 = obstacle.getPosition().x - ownerPosition.x; dx2 *= dx2;
            double dy2 = obstacle.getPosition().y - ownerPosition.y; dy2 *= dy2;

            // If the organism is outside the visible circle, skip it
            if (dx2 + dy2 > distanceSquared) continue;

            filtered.add(obstacle);
        }

        return filtered;
    }
}