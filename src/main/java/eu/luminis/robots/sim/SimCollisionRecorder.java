package eu.luminis.robots.sim;

import eu.luminis.events.EventBroadcaster;
import eu.luminis.events.EventType;
import eu.luminis.evolution.CostCalculator;
import eu.luminis.geometry.Vector;

/**
 * Records collisions
 */
public class SimCollisionRecorder {
    private final static EventBroadcaster eventBroadcaster = EventBroadcaster.getInstance();
    private final static CostCalculator costCalculator = CostCalculator.getInstance();

    private final SimWorld world;
    private final SimMovementRecorder movementRecorder;
    private final double size;
    private final double viewDistance;
    private Vector targetObstaclePosition;
    private double collisionDamage = 0;

    public SimCollisionRecorder(SimWorld world, SimMovementRecorder movementRecorder, double size, double viewDistance) {
        this.world = world;
        this.movementRecorder = movementRecorder;
        this.size = size;
        this.viewDistance = viewDistance;
    }

    public void recordCollision() {
        Vector velocity = movementRecorder.getVelocity();
        collisionDamage += costCalculator.collide(velocity.getLength());

        movementRecorder.preventOverlap();

        eventBroadcaster.broadcast(EventType.COLLIDE, collisionDamage);
    }

    public Vector getTargetObstaclePosition() {
        return targetObstaclePosition;
    }

    public void setTargetObstaclePosition(Vector position) {
        this.targetObstaclePosition = position;
    }

    public SimWorld getWorld() {
        return world;
    }

    public double getSize() {
        return size;
    }

    public double getViewDistance() {
        return viewDistance;
    }

    public double getCollisionDamage() {
        return collisionDamage;
    }
}
