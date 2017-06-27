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
            return distanceToLeftWall(robotPosition);
        }

        if (isRightWallVisible(robotPosition, viewingAngle, angleRadius)) {
            return distanceToRightWall(robotPosition);
        }

        if (isTopWallVisible(robotPosition, viewingAngle, angleRadius)) {
            return distanceToTopWall(robotPosition);
        }

        if (isBottomWallVisible(robotPosition, viewingAngle, angleRadius)) {
            return distanceToBottomWall(robotPosition);
        }

        return viewDistance;
    }

    private boolean isBottomWallVisible(Vector robotPosition, double viewingAngle, double angleRadius) {
        return distanceToBottomWall(robotPosition) < viewDistance &&
                isLookingAt(0.5 * Math.PI, angleRadius, viewingAngle);
    }

    private boolean isTopWallVisible(Vector robotPosition, double viewingAngle, double angleRadius) {
        return distanceToTopWall(robotPosition) < viewDistance &&
                isLookingAt(1.5 * Math.PI, angleRadius, viewingAngle);
    }

    private boolean isRightWallVisible(Vector robotPosition, double viewingAngle, double angleRadius) {
        return distanceToRightWall(robotPosition) < viewDistance &&
                isLookingAt(0, angleRadius, viewingAngle);
    }

    private boolean isLeftWallVisible(Vector robotPosition, double viewingAngle, double angleRadius) {
        return distanceToLeftWall(robotPosition) < viewDistance &&
                isLookingAt(Math.PI, angleRadius, viewingAngle);
    }

    private boolean isLookingAt(double angle, double angleRadius, double viewingAngle) {
        return angle - angleRadius < viewingAngle && viewingAngle < angle + angleRadius;
    }

    private boolean collidesWithAny(List<SimObstacle> simObstacles) {
        for (SimObstacle simObstacle : simObstacles) {
            if (collidesWith(simObstacle)) {
                return true;
            }
        }

        return false;
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

        boolean colliding =
                    distanceToBottomWall(robotPosition) <= robotRadius ||
                    distanceToTopWall(robotPosition) <= robotRadius ||
                    distanceToRightWall(robotPosition) <= robotRadius ||
                    distanceToLeftWall(robotPosition) <= robotRadius;

        if (!colliding) return false;

        collisionRecorder.recordCollision();

        return true;
    }

    private double distanceToBottomWall(Vector robotPosition) {
        return collisionRecorder.getWorld().getMaxY() - robotPosition.getY();
    }

    private double distanceToTopWall(Vector robotPosition) {
        return robotPosition.getY() - collisionRecorder.getWorld().getMinY();
    }

    private double distanceToRightWall(Vector robotPosition) {
        return collisionRecorder.getWorld().getMaxX() - robotPosition.getX();
    }

    private double distanceToLeftWall(Vector robotPosition) {
        return robotPosition.getX() - collisionRecorder.getWorld().getMinX();
    }
}
