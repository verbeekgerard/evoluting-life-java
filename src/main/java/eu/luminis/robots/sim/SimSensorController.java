package eu.luminis.robots.sim;

import eu.luminis.geometry.Position;
import eu.luminis.geometry.Radians;
import eu.luminis.robots.core.IAngleRetriever;
import eu.luminis.robots.core.ISensorController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

class SimSensorController implements ISensorController {
    private final static CollisionDetector collisionDetector = new CollisionDetector();

    private final SimRobot owner;
    private final double viewDistance;
    private final double viewAngle = Math.PI;
    private final IAngleRetriever angleRetriever;
    private final SensorFilter sensorFilter;

    private List<SimObstacle> nearbySimObstacles;

    public SimSensorController(SimRobot owner, double viewDistance, IAngleRetriever angleRetriever) {
        this.owner = owner;
        this.viewDistance = viewDistance;
        this.angleRetriever = angleRetriever;
        this.sensorFilter = new SensorFilter(owner, viewDistance);
    }

    public void prepareForNearbyObstacles() {
        List<SimObstacle> simObstacles = owner.getWorld().getAllObstacles();
        nearbySimObstacles = sensorFilter.filter(simObstacles);
    }

    @Override
    public double sense() {
        double sensorAngle = angleRetriever.getAngle();
        Optional<ObstacleVector> seeing = getObstacleVectorsWithinSight(nearbySimObstacles)
                .stream()
                .filter(obstacleVector -> isLookingAt(sensorAngle, obstacleVector))
                .sorted(Comparator.comparing(ObstacleVector::getDistance))
                .findFirst();

        return Math.min(wallDistance(), seeing.isPresent() ?
                seeing.get().getDistance() :
                viewDistance);
    }

    @Override
    public double getViewDistance() {
        return viewDistance;
    }

    public boolean isColliding() {
        return collidesWithAny(nearbySimObstacles);
    }

    private boolean isLookingAt(double sensorAngle, ObstacleVector obstacleWithinSight) {
        double angularRadius = Math.atan2(obstacleWithinSight.getSize() / 2, obstacleWithinSight.getDistance());
        double minimumBorderAngle = obstacleWithinSight.getAngle() - angularRadius;
        double maximumBorderAngle = obstacleWithinSight.getAngle() + angularRadius;

        return minimumBorderAngle <= sensorAngle && sensorAngle <= maximumBorderAngle;
    }

    private List<ObstacleVector> getObstacleVectorsWithinSight(List<? extends SimObstacle> obstacles) {
        Position ownerPosition = owner.getPosition();
        List<ObstacleVector> obstacleVectors = new ArrayList<>();

        for (SimObstacle simObstacle : obstacles) {
            double globalAngle = ownerPosition.calculateAngle(simObstacle.getPosition());
            double relativeAngle = Radians.getRelativeDifference(ownerPosition.a, globalAngle);
            double distance = ownerPosition.calculateDistance(simObstacle.getPosition());

            // If the obstacle is outside the field of view, skip it
            if (Math.abs(relativeAngle) > viewAngle / 2 || distance > viewDistance) continue;

            obstacleVectors.add(new ObstacleVector(distance, relativeAngle, simObstacle.getSize()));
        }

        return obstacleVectors;
    }

    private double wallDistance() {
        Position ownerPosition = owner.getPosition();

        double viewingAngle = Radians.getBounded(ownerPosition.a + angleRetriever.getAngle());
        double angleRadius = Math.PI / 10;

        if (isLeftWallVisible(ownerPosition, viewingAngle, angleRadius)) {
            return ownerPosition.x - owner.getWorld().getMinX();
        }

        if (isRightWallVisible(ownerPosition, viewingAngle, angleRadius)) {
            return owner.getWorld().getMaxX() - ownerPosition.x;
        }

        if (isTopWallVisible(ownerPosition, viewingAngle, angleRadius)) {
            return ownerPosition.y - owner.getWorld().getMinY();
        }

        if (isBottomWallVisible(ownerPosition, viewingAngle, angleRadius)) {
            return owner.getWorld().getMaxY() - ownerPosition.y;
        }

        return viewDistance;
    }

    private boolean isBottomWallVisible(Position ownerPosition, double viewingAngle, double angleRadius) {
        return owner.getWorld().getMaxY() - ownerPosition.y < viewDistance &&
                isLookingAt(0.5 * Math.PI, angleRadius, viewingAngle);
    }

    private boolean isTopWallVisible(Position ownerPosition, double viewingAngle, double angleRadius) {
        return ownerPosition.y - owner.getWorld().getMinY() < viewDistance &&
                isLookingAt(1.5 * Math.PI, angleRadius, viewingAngle);
    }

    private boolean isRightWallVisible(Position ownerPosition, double viewingAngle, double angleRadius) {
        return owner.getWorld().getMaxX() - ownerPosition.x < viewDistance &&
                isLookingAt(0, angleRadius, viewingAngle);
    }

    private boolean isLeftWallVisible(Position ownerPosition, double viewingAngle, double angleRadius) {
        return ownerPosition.x - owner.getWorld().getMinX() < viewDistance &&
                isLookingAt(Math.PI, angleRadius, viewingAngle);
    }

    private boolean isLookingAt(double angle, double angleRadius, double viewingAngle) {
        return angle - angleRadius < viewingAngle && viewingAngle < angle + angleRadius;
    }

    private boolean collidesWithAny(List<SimObstacle> simObstacles) {
        boolean isColliding = false;

        for (SimObstacle simObstacle : simObstacles) {
            isColliding = isColliding || collidesWith(simObstacle);
        }

        return isColliding;
    }

    private boolean collidesWith(SimObstacle simObstacle) {
        boolean colliding = collisionDetector.colliding(owner, simObstacle);
        if (!colliding) return false;

        owner.recordCollision();

        return true;
    }
}
