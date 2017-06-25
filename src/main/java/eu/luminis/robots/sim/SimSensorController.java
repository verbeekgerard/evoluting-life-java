package eu.luminis.robots.sim;

import eu.luminis.geometry.Position;
import eu.luminis.geometry.Radians;
import eu.luminis.geometry.Velocity;
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

    private final SimMovementRecorder movementRecorder;

    private List<SimObstacle> nearbySimObstacles;

    public SimSensorController(SimRobot owner, SimMovementRecorder movementRecorder, double viewAngle, double viewDistance, IAngleRetriever angleRetriever) {
        this.owner = owner;
        this.movementRecorder = movementRecorder;
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
        Position robotPosition = movementRecorder.getPosition();
        Velocity robotVelocity = movementRecorder.getVelocity();

        List<ObstacleVector> obstacleVectors = new ArrayList<>();

        for (SimObstacle simObstacle : obstacles) {
            Position obstaclePosition = simObstacle.getPosition();

            double globalAngle = robotPosition.calculateAngle(obstaclePosition);
            double relativeAngle = Radians.getRelativeDifference(robotVelocity.getAngle(), globalAngle);
            double distance = robotPosition.calculateDistance(obstaclePosition);

            // If the obstacle is outside the field of view, skip it
            if (Math.abs(relativeAngle) > viewAngle / 2 || distance > viewDistance) continue;

            obstacleVectors.add(new ObstacleVector(distance, relativeAngle, simObstacle.getSize(), obstaclePosition));
        }

        return obstacleVectors;
    }

    private double wallDistance() {
        Position robotPosition = movementRecorder.getPosition();
        Velocity robotVelocity = movementRecorder.getVelocity();

        double viewingAngle = Radians.getBounded(robotVelocity.getAngle() + angleRetriever.getAngle());
        double angleRadius = Math.PI / 10;

        if (isLeftWallVisible(robotPosition, viewingAngle, angleRadius)) {
            return robotPosition.getX() - owner.getWorld().getMinX();
        }

        if (isRightWallVisible(robotPosition, viewingAngle, angleRadius)) {
            return owner.getWorld().getMaxX() - robotPosition.getX();
        }

        if (isTopWallVisible(robotPosition, viewingAngle, angleRadius)) {
            return robotPosition.getY() - owner.getWorld().getMinY();
        }

        if (isBottomWallVisible(robotPosition, viewingAngle, angleRadius)) {
            return owner.getWorld().getMaxY() - robotPosition.getY();
        }

        return viewDistance;
    }

    private boolean isBottomWallVisible(Position robotPosition, double viewingAngle, double angleRadius) {
        return owner.getWorld().getMaxY() - robotPosition.getY() < viewDistance &&
                isLookingAt(0.5 * Math.PI, angleRadius, viewingAngle);
    }

    private boolean isTopWallVisible(Position robotPosition, double viewingAngle, double angleRadius) {
        return robotPosition.getY() - owner.getWorld().getMinY() < viewDistance &&
                isLookingAt(1.5 * Math.PI, angleRadius, viewingAngle);
    }

    private boolean isRightWallVisible(Position robotPosition, double viewingAngle, double angleRadius) {
        return owner.getWorld().getMaxX() - robotPosition.getX() < viewDistance &&
                isLookingAt(0, angleRadius, viewingAngle);
    }

    private boolean isLeftWallVisible(Position robotPosition, double viewingAngle, double angleRadius) {
        return robotPosition.getX() - owner.getWorld().getMinX() < viewDistance &&
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
        Position robotPosition = movementRecorder.getPosition();
        double robotRadius = owner.getSize() / 2;
        SimWorld world = owner.getWorld();

        boolean colliding =
                robotPosition.getX() - world.getMinX() <= robotRadius ||
                world.getMaxX() - robotPosition.getX() <= robotRadius ||
                robotPosition.getY() - world.getMinY() <= robotRadius ||
                world.getMaxY() - robotPosition.getY() <= robotRadius;

        if (!colliding) return false;

        owner.recordCollision();

        return true;
    }
}
