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
        List<Organism> obstacles = new ArrayList<>();
        obstacles.addAll(filter(plants));
        obstacles.addAll(filter(animals));

        return obstacles;
    }

    private List<Organism> filter(List<? extends Organism> organisms) {
        Position p = owner.getPosition();

        // An array of vectors to foods from this entity's perspective
        List<Organism> filteredOrganisms = new ArrayList<>();

        // Loop through foodSupply
        for (Organism organism : organisms) {
            if (organism == this.owner) continue;

            // Find polar coordinates of food relative this entity
            double dx2 = organism.getPosition().x - p.x; dx2 *= dx2;
            double dy2 = organism.getPosition().y - p.y; dy2 *= dy2;

            // Check bounding box first for performance
            if (dx2 + dy2 > distanceSquared) continue;

            filteredOrganisms.add(organism);
        }

        return filteredOrganisms;
    }
}