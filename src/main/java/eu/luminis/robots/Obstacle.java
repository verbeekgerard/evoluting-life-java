package eu.luminis.robots;

import eu.luminis.entities.Position;
import eu.luminis.entities.World;
import eu.luminis.general.EventBroadcaster;

public abstract class Obstacle {
    private World world;
    private Position position;
    protected EventBroadcaster eventBroadcaster = EventBroadcaster.getInstance();

    public Obstacle(World world, Position position) {
        this.world = world;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
    public abstract double getSize();
}
