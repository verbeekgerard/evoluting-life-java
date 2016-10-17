package eu.luminis.robots;

import eu.luminis.entities.CollisionDetector;
import eu.luminis.entities.Position;
import eu.luminis.entities.SensorFilter;
import eu.luminis.sensors.ObstacleVector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SimSensorController implements ISensorController {
    private SimRobot owner;
    private double viewDistance;
    private IAngleRetriever angleRetriever;
    private SensorFilter sensorFilter;
    private CollisionDetector collisionDetector = new CollisionDetector();

    private List<Obstacle> nearbyObstacles;

    public SimSensorController(SimRobot owner, double viewDistance, IAngleRetriever angleRetriever) {
        this.owner = owner;
        this.viewDistance = viewDistance;
        this.angleRetriever = angleRetriever;
        this.sensorFilter = new SensorFilter(owner, viewDistance);
    }

    @Override
    public double sense() {
        double viewingAngle = angleRetriever.getAngle();
        Optional<ObstacleVector> seeing = getObstacleVectorsWithinSight(nearbyObstacles)
                .stream()
                .filter(obstacleVector -> isLookingAt(viewingAngle, obstacleVector))
                .sorted(Comparator.comparing(ObstacleVector::getDistance))
                .findFirst();

        return seeing.isPresent() ?
                seeing.get().getDistance() :
                viewDistance;
    }

    private boolean isLookingAt(double viewingAngle, ObstacleVector obstacleWithinSight) {
        double angularRadius = Math.atan2(obstacleWithinSight.getSize(), obstacleWithinSight.getDistance());
        double minimumBorderAngle = obstacleWithinSight.getAngle() - angularRadius;
        double maximumBorderAngle = obstacleWithinSight.getAngle() + angularRadius;

        return viewingAngle >= minimumBorderAngle && viewingAngle <= maximumBorderAngle;
    }

    private List<ObstacleVector> getObstacleVectorsWithinSight(List<? extends Obstacle> obstacles) {
        Position ownerPosition = owner.getPosition();
        List<ObstacleVector> obstacleVectors = new ArrayList<>();

        for (Obstacle obstacle : obstacles) {
            // Find polar coordinates of food relative this entity
            double dx = obstacle.getPosition().x - ownerPosition.x;
            double dy = obstacle.getPosition().y - ownerPosition.y;

            // Find angle of food relative to entity
            if (dx == 0) dx = 0.000000000001;
            double angle = ownerPosition.a - Math.atan2(dy, dx);

            // Convert angles to right of center into negative values
            if (angle > Math.PI) angle -= 2 * Math.PI;

            // Calculate distance to this food
            double distance = Math.sqrt(dx * dx + dy * dy);

            // If the food is outside the viewing range, skip it
            if (Math.abs(angle) > Math.PI / 2 || distance > this.viewDistance) continue;

            obstacleVectors.add(new ObstacleVector(distance, angle, obstacle.getSize()));
        }

        return obstacleVectors;
    }

    public boolean isColliding() {
        return collidesWithAny(nearbyObstacles);
    }

    public void prepareForNearbyObstacles() {
        List<Obstacle> obstacles = owner.getWorld().getAllObstacles();
        nearbyObstacles = sensorFilter.filter(obstacles);
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
