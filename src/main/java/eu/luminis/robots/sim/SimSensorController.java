package eu.luminis.robots.sim;

import eu.luminis.geometry.Radians;
import eu.luminis.geometry.Vector;
import eu.luminis.robots.core.IAngleRetriever;
import eu.luminis.robots.core.ISensorController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

class SimSensorController implements ISensorController {
    private final static CollisionDetector collisionDetector = new CollisionDetector();

    private final double viewDistance;
    private final double viewAngle;
    private final IAngleRetriever angleRetriever;
    private final SensorFilter sensorFilter;

    private final SimCollisionRecorder collisionRecorder;
    private final SimMovementRecorder movementRecorder;

    private List<SimObstacle> nearbySimObstacles;

    public SimSensorController(SimCollisionRecorder collisionRecorder, SimMovementRecorder movementRecorder, double viewAngle, IAngleRetriever angleRetriever) {
        this.collisionRecorder = collisionRecorder;
        this.movementRecorder = movementRecorder;
        this.viewAngle = viewAngle;
        this.viewDistance = collisionRecorder.getViewDistance();
        this.angleRetriever = angleRetriever;
        this.sensorFilter = new SensorFilter(movementRecorder, collisionRecorder);
    }

    public void prepareForNearbyObstacles() {
        List<SimObstacle> simObstacles = collisionRecorder.getWorld().getAllObstacles(); // TODO: Get the obstacles from somewhere else
        nearbySimObstacles = sensorFilter.filter(simObstacles);
    }

    @Override
    public double sense() {
        double sensorAngle = angleRetriever.getAngle();
        Optional<ObstacleVector> seeing = getObstacleVectorsWithinSight(nearbySimObstacles)
                .stream()
                .filter(obstacleVector -> isLookingAt(sensorAngle, obstacleVector))
                .sorted(Comparator.comparing(ObstacleVector::getSquaredDistance))
                .findFirst();

        ObstacleVector obstacleVector = seeing.orElse(null);
        collisionRecorder.setTargetObstaclePosition(obstacleVector == null ?
                null :
                obstacleVector.getPosition());

        return Math.min(wallDistance(), obstacleVector == null ?
                viewDistance :
                Math.sqrt(obstacleVector.getSquaredDistance()));
    }

    @Override
    public double getViewDistance() {
        return viewDistance;
    }

    public boolean isColliding() {
        return collidesWithAny(nearbySimObstacles) || collidesWithWall();
    }

    private boolean isLookingAt(double sensorAngle, ObstacleVector obstacleWithinSight) {
        double angularRadius = Math.atan2(obstacleWithinSight.getSize() / 2, Math.sqrt(obstacleWithinSight.getSquaredDistance()));
        double minimumBorderAngle = obstacleWithinSight.getAngle() - angularRadius;
        double maximumBorderAngle = obstacleWithinSight.getAngle() + angularRadius;

        return minimumBorderAngle <= sensorAngle && sensorAngle <= maximumBorderAngle;
    }

    private List<ObstacleVector> getObstacleVectorsWithinSight(List<? extends SimObstacle> obstacles) {
        Vector robotPosition = movementRecorder.getPosition();
        Vector robotVelocity = movementRecorder.getVelocity();

        List<ObstacleVector> obstacleVectors = new ArrayList<>();

        for (SimObstacle simObstacle : obstacles) {
            Vector obstaclePosition = simObstacle.getPosition();
            ObstacleVector obstacleVector = new ObstacleVector(robotPosition, robotVelocity, obstaclePosition, simObstacle.getSize());

            // If the obstacle is outside the field of view, skip it
            if (Math.abs(obstacleVector.getAngle()) > viewAngle / 2 || obstacleVector.getSquaredDistance() > viewDistance * viewDistance)
                continue;

            obstacleVectors.add(obstacleVector);
        }

        return obstacleVectors;
    }

    private double wallDistance() {
        Vector robotPosition = movementRecorder.getPosition();
        Vector robotVelocity = movementRecorder.getVelocity();

        double viewingAngle = Radians.getBounded(robotVelocity.getAngle() + angleRetriever.getAngle());
        double angleRadius = Math.PI / 10;

        if (isLeftWallVisible(robotPosition, viewingAngle, angleRadius)) {
            return robotPosition.getX() - collisionRecorder.getWorld().getMinX();
        }

        if (isRightWallVisible(robotPosition, viewingAngle, angleRadius)) {
            return collisionRecorder.getWorld().getMaxX() - robotPosition.getX();
        }

        if (isTopWallVisible(robotPosition, viewingAngle, angleRadius)) {
            return robotPosition.getY() - collisionRecorder.getWorld().getMinY();
        }

        if (isBottomWallVisible(robotPosition, viewingAngle, angleRadius)) {
            return collisionRecorder.getWorld().getMaxY() - robotPosition.getY();
        }

        return viewDistance;
    }

    private boolean isBottomWallVisible(Vector robotPosition, double viewingAngle, double angleRadius) {
        return collisionRecorder.getWorld().getMaxY() - robotPosition.getY() < viewDistance &&
                isLookingAt(0.5 * Math.PI, angleRadius, viewingAngle);
    }

    private boolean isTopWallVisible(Vector robotPosition, double viewingAngle, double angleRadius) {
        return robotPosition.getY() - collisionRecorder.getWorld().getMinY() < viewDistance &&
                isLookingAt(1.5 * Math.PI, angleRadius, viewingAngle);
    }

    private boolean isRightWallVisible(Vector robotPosition, double viewingAngle, double angleRadius) {
        return collisionRecorder.getWorld().getMaxX() - robotPosition.getX() < viewDistance &&
                isLookingAt(0, angleRadius, viewingAngle);
    }

    private boolean isLeftWallVisible(Vector robotPosition, double viewingAngle, double angleRadius) {
        return robotPosition.getX() - collisionRecorder.getWorld().getMinX() < viewDistance &&
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
        boolean colliding = collisionDetector.colliding(
                movementRecorder.getPosition(),
                simObstacle.getPosition(),
                collisionRecorder.getSize(),
                simObstacle.getSize());

        if (!colliding) return false;

        collisionRecorder.recordCollision();

        return true;
    }

    private boolean collidesWithWall() {
        Vector robotPosition = movementRecorder.getPosition();
        double robotRadius = collisionRecorder.getSize() / 2;
        SimWorld world = collisionRecorder.getWorld();

        boolean colliding =
                robotPosition.getX() - world.getMinX() <= robotRadius ||
                world.getMaxX() - robotPosition.getX() <= robotRadius ||
                robotPosition.getY() - world.getMinY() <= robotRadius ||
                world.getMaxY() - robotPosition.getY() <= robotRadius;

        if (!colliding) return false;

        collisionRecorder.recordCollision();

        return true;
    }
}
