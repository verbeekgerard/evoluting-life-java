package eu.luminis.sensors;

import eu.luminis.entities.*;
import eu.luminis.genetics.SensorGene;

import java.util.ArrayList;
import java.util.List;

public class Eyes {
    private World world;
    private Animal owner;
    private double viewDistance;
    private double fieldOfView;

    private WallDistanceSensor wallDistanceSensor;

    public Eyes(Animal owner, SensorGene sensorGen, World world) {
        this.world = world;
        this.owner = owner;
        this.viewDistance = sensorGen.getViewDistance();
        this.fieldOfView = sensorGen.getFieldOfView();

        this.wallDistanceSensor = new WallDistanceSensor(this.world, this.viewDistance);
    }

    public double getViewDistance() {
        return viewDistance;
    }

    public double getFieldOfView() {
        return fieldOfView;
    }

    public Obstacles sense(List<Organism> organisms) {
        List<ObstacleVector> obstacles = new ArrayList<>();
        obstacles.addAll(findOrganisms(organisms));

        double wallDistance = wallDistanceSensor.calculate(owner.getPosition());

        return new Obstacles(obstacles, wallDistance);
    }

    private List<ObstacleVector> findOrganisms(List<? extends Organism> organisms) {
        Position ownerPosition = owner.getPosition();
        List<ObstacleVector> obstacleVectors = new ArrayList<>();

        for (Organism organism : organisms) {
            // Find polar coordinates of food relative this entity
            double dx = organism.getPosition().x - ownerPosition.x;
            double dy = organism.getPosition().y - ownerPosition.y;

            // Find angle of food relative to entity
            if (dx == 0) dx = 0.000000000001;
            double angle = ownerPosition.a - Math.atan2(dy, dx);

            // Convert angles to right of center into negative values
            if (angle > Math.PI) angle -= 2 * Math.PI;

            // Calculate distance to this food
            double distance = Math.sqrt(dx * dx + dy * dy);

            // If the food is outside the viewing range, skip it
            if (Math.abs(angle) > this.fieldOfView / 2 || distance > this.viewDistance) continue;

            obstacleVectors.add(new ObstacleVector(distance, angle, organism));
        }

        return obstacleVectors;
    }
}