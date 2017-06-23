package eu.luminis.robots.sim;

import eu.luminis.geometry.Position;
import eu.luminis.events.EventBroadcaster;
import eu.luminis.events.EventType;
import eu.luminis.genetics.LifeGene;

public abstract class SimObstacle {
    protected final static EventBroadcaster eventBroadcaster = EventBroadcaster.getInstance();

    private final SimWorld world;
    private final Position position;
    private final SimLife life;

    public SimObstacle(SimWorld world, Position position, SimLife life) {
        this.world = world;
        this.position = position;
        this.life = life;
    }

    final public void runCycle() {
        life.recordLifeCycle();
        run();
    }

    public final SimWorld getWorld() {
        return world;
    }

    public final Position getPosition() {
        return position;
    }

    public abstract double getSize();

    public final boolean survives() {
        if (life.isOverOldAge()) {
            eventBroadcaster.broadcast(EventType.DIED_OF_AGE, 1);
            return false;
        }

        if (!world.isWithinBorders(position)) {
            eventBroadcaster.broadcast(EventType.WANDERED, 1);
            return false;
        }

        if (!isAlive()) {
            eventBroadcaster.broadcast(EventType.STARVED, 1);
            return false;
        }

        return true;
    }

    public IAgeRetriever getAgeInformation() {
        return life;
    }

    protected abstract void run();

    protected boolean isAlive() {
        return true;
    }
}
