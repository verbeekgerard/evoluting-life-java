package eu.luminis.entities;

import java.util.ArrayList;
import java.util.List;

class SensorFilter {
    private final Animal owner;
    private final double distanceSquared;

    public SensorFilter(Animal owner, double viewDistance) {
        this.owner = owner;

        double ds2 = Math.max(owner.getSize(), viewDistance); ds2 *= ds2;
        this.distanceSquared = ds2;
    }

    public List<Organism> filter(List<Plant> plants, List<Animal> animals) {
        List<Organism> filteredOrganisms = new ArrayList<>();
        filteredOrganisms.addAll(filter(plants));
        filteredOrganisms.addAll(filter(animals));

        return filteredOrganisms;
    }

    private List<Organism> filter(List<? extends Organism> organisms) {
        Position ownerPosition = owner.getPosition();
        List<Organism> filteredOrganisms = new ArrayList<>();

        for (Organism organism : organisms) {
            if (organism == this.owner) continue;

            // Compute the squared x and y differences
            double dx2 = organism.getPosition().x - ownerPosition.x; dx2 *= dx2;
            double dy2 = organism.getPosition().y - ownerPosition.y; dy2 *= dy2;

            // If the organism is outside the visible circle, skip it
            if (dx2 + dy2 > distanceSquared) continue;

            filteredOrganisms.add(organism);
        }

        return filteredOrganisms;
    }
}