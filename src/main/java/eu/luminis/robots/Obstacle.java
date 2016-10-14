package eu.luminis.robots;

import eu.luminis.entities.Position;
import eu.luminis.entities.World;

public abstract class Obstacle {
    private World world;
    private Position position;

    public Obstacle(World world, Position position) {
        this.world = world;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
