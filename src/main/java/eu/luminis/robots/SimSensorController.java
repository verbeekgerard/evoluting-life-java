package eu.luminis.robots;

import eu.luminis.entities.CollisionDetector;
import eu.luminis.entities.SensorFilter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class SimSensorController implements ISensorController {
    private SimRobot owner;
    private SensorFilter sensorFilter;
    private CollisionDetector collisionDetector = new CollisionDetector();


    public SimSensorController(SimRobot owner, double viewDistance) {
        this.owner = owner;
        this.sensorFilter = new SensorFilter(owner, viewDistance);
    }

    @Override
    public Double sense() {
        // TODO: find out if there are obstacles in the line of view
        throw new NotImplementedException();
    }

    public boolean isColliding() {
        List<Obstacle> obstacles = owner.getWorld().getAllObstacles();
        List<Obstacle> nearbyObstacles = sensorFilter.filter(obstacles);

        return collidesWithAny(nearbyObstacles);
    }

    private boolean collidesWithAny(List<Obstacle> obstacles) {
        boolean isColliding = false;

        for (Obstacle obstacle : obstacles) {
            isColliding = isColliding || collidesWith(obstacle);
        }

        return isColliding;
    }

    private boolean collidesWith(Obstacle obstacle) {
        boolean colliding = collisionDetector.colliding(owner, obstacle);
        if (!colliding) return false;

        owner.recordCollision();

        return true;
    }
}
