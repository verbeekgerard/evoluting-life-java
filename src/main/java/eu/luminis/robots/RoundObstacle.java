package eu.luminis.robots;

import eu.luminis.entities.Position;

public class RoundObstacle extends Obstacle {
    public RoundObstacle(SimWorld world, Position position) {
        super(world, position);
    }

    @Override
    public double getSize() {
        return 0;
    }
}
