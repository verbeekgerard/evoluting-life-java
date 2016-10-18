package eu.luminis.robots;

import eu.luminis.entities.Position;
import eu.luminis.general.EventBroadcaster;

public abstract class Obstacle {
    protected SimWorld world;
    private Position position;
    protected EventBroadcaster eventBroadcaster = EventBroadcaster.getInstance();

    public Obstacle(SimWorld world, Position position) {
        this.world = world;
        this.position = position;
    }

    public SimWorld getWorld() {
        return world;
    }

    public Position getPosition() {
        return position;
    }

    public abstract double getSize();
}
