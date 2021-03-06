package eu.luminis.robots.sim;

import eu.luminis.events.EventBroadcaster;
import eu.luminis.events.EventType;
import eu.luminis.geometry.IBorderDimensions;
import eu.luminis.geometry.Vector;

public abstract class SimObstacle {
    protected final static EventBroadcaster eventBroadcaster = EventBroadcaster.getInstance();

    private final IBorderDimensions world;
    private final SimMovementRecorder movementRecorder;
    private final SimLife life;

    public SimObstacle(IBorderDimensions world, SimMovementRecorder movementRecorder, SimLife life) {
        this.world = world;
        this.movementRecorder = movementRecorder;
        this.life = life;
    }

    final public void runCycle() {
        life.recordLifeCycle();
        run();
    }

    public final Vector getPosition() {
        return movementRecorder.getPosition();
    }

    public final Vector getVelocity() {
        return movementRecorder.getVelocity();
    }

    public abstract double getSize();

    public final boolean survives() {
        if (life.isOverOldAge()) {
            eventBroadcaster.broadcast(EventType.DIED_OF_AGE, 1);
            return false;
        }

        if (!world.isWithinBorders(movementRecorder.getPosition())) {
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
