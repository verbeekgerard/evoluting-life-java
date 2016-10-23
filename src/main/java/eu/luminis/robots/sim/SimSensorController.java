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
    private final double viewAngle;
    private final IAngleRetriever angleRetriever;
    private final SensorFilter sensorFilter;

    private List<SimObstacle> nearbySimObstacles;

    public SimSensorController(SimRobot owner, double viewAngle, double viewDistance, IAngleRetriever angleRetriever) {
        this.owner = owner;
        this.viewAngle = viewAngle;
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

        ObstacleVector obstacleVector = seeing.isPresent() ? seeing.get() : null;
        owner.setTargetObstaclePosition(obstacleVector == null ?
                null :
                obstacleVector.getPosition());

        return Math.min(wallDistance(), obstacleVector == null ?
                viewDistance :
                obstacleVector.getDistance());
    }

    @Override
    public double getViewDistance() {
        return viewDistance;
    }

    public boolean isColliding() {
        return collidesWithAny(nearbySimObstacles) || collidesWithWall();
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
            Position obstaclePosition = simObstacle.getPosition();

            double globalAngle = ownerPosition.calculateAngle(obstaclePosition);
            double relativeAngle = Radians.getRelativeDifference(ownerPosition.a, globalAngle);
            double distance = ownerPosition.calculateDistance(obstaclePosition);

            // If the obstacle is outside the field of view, skip it
            if (Math.abs(relativeAngle) > viewAngle / 2 || distance > viewDistance) continue;

            obstacleVectors.add(new ObstacleVector(distance, relativeAngle, simObstacle.getSize(), obstaclePosition));
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

    private boolean collidesWithWall() {
        Position ownerPosition = owner.getPosition();
        double ownerRadius = owner.getSize() / 2;
        SimWorld world = owner.getWorld();

        boolean colliding =
                ownerPosition.x - world.getMinX() <= ownerRadius ||
                world.getMaxX() - ownerPosition.x <= ownerRadius ||
                ownerPosition.y - world.getMinY() <= ownerRadius ||
                world.getMaxY() - ownerPosition.y <= ownerRadius;

        if (!colliding) return false;

        owner.recordCollision();

        return true;
    }
}
