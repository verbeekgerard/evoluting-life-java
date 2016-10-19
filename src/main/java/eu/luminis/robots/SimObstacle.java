package eu.luminis.robots;

import eu.luminis.entities.Position;
import eu.luminis.general.EventBroadcaster;
import eu.luminis.general.EventType;
import eu.luminis.genetics.LifeGene;

public abstract class SimObstacle {
    private final SimWorld world;
    private final Position position;
    private final SimLife life;
    protected EventBroadcaster eventBroadcaster = EventBroadcaster.getInstance();

    public SimObstacle(SimWorld world, Position position, LifeGene lifeGene) {
        this.world = world;
        this.position = position;
        this.life = new SimLife((int)lifeGene.getOldAge());
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

    protected abstract void run();

    protected boolean isAlive() {
        return true;
    }
}
