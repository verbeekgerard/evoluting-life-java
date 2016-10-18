package eu.luminis.sensors;

import eu.luminis.entities.Position;
import eu.luminis.robots.SimWorld;

class WallDistanceSensor {
    private double leftWall = 0;
    private double topWall = 0;
    private double rightWall;
    private double bottomWall;

    private double viewDistance;

    public WallDistanceSensor(SimWorld world, double viewDistance) {
        this.rightWall = world.getWidth();
        this.bottomWall = world.getHeight();
        this.viewDistance = viewDistance;
    }

    public Double calculate(Position position) {
        // If not near to a wall
        if (position.x > this.viewDistance &&
            rightWall - position.x > this.viewDistance &&
            position.y > this.viewDistance &&
            bottomWall - position.y > this.viewDistance) {
            return this.viewDistance;
        }

        // Keep angles within bounds
        position.a = position.a % (Math.PI * 2);
        if (position.a < 0) {
            position.a += Math.PI * 2;
        }

        // cosine(angle) = adjacent / hypotenuse
        // hypotenuse = adjacent / cosine(angle)

        // sine(angle) = opposite / hypotenuse
        // hypotenuse = opposite / sine(angle)

        if (isFacingRightOrBottomWall(position)) {
            return getDistanceToRightOrBottomWall(position);
        }

        if (isFacingLeftOrBottomWall(position)) {
            return getDistanceToLeftOrBottomWall(position);
        }

        if (isFacingLeftOrTopWall(position)) {
            return getDistanceToLeftOrTopWall(position);
        }

        if (isFacingRightOrTopWall(position)) {
            return getDistanceToRightOrTopWall(position);
        }

        throw new Error("Impossible situation for position: " + position.toString());
    }

    private boolean isFacingRightOrTopWall(Position p) {
        // Facing right wall, top wall: x=world.width, y=0
        return p.a >= Math.PI * 1.5 && p.a < Math.PI * 2;
    }

    private double getDistanceToRightOrTopWall(Position p) {
        double distX = (rightWall - p.x) / Math.cos(p.a);
        double distY = (topWall - p.y) / Math.sin(p.a);

        return Math.min(Math.min(distX, distY), this.viewDistance);
    }

    private boolean isFacingLeftOrTopWall(Position p) {
        // Facing left wall, top wall: x=0, y=0
        return p.a >= Math.PI && p.a <= Math.PI * 1.5;
    }

    private double getDistanceToLeftOrTopWall(Position p) {
        double distX = (leftWall - p.x) / Math.cos(p.a);
        double distY = (topWall - p.y) / Math.sin(p.a);

        return Math.min(Math.min(distX, distY), this.viewDistance);
    }

    private boolean isFacingLeftOrBottomWall(Position p) {
        // Facing left wall, bottom wall: x=0, y=world.height
        return p.a >= Math.PI * 0.5 && p.a <= Math.PI;
    }

    private double getDistanceToLeftOrBottomWall(Position p) {
        double distX = (leftWall - p.x) / Math.cos(p.a);
        double distY = (bottomWall - p.y) / Math.sin(p.a);

        return Math.min(Math.min(distX, distY), this.viewDistance);
    }

    private boolean isFacingRightOrBottomWall(Position p) {
        // Facing right wall, bottom wall: x=world.width, y=world.height
        return p.a >= 0 && p.a <= Math.PI * 0.5;
    }

    private double getDistanceToRightOrBottomWall(Position p) {
        double distX = (rightWall - p.x) / Math.cos(p.a);
        double distY = (bottomWall - p.y) / Math.sin(p.a);

        return Math.min(Math.min(distX, distY), this.viewDistance);
    }
}