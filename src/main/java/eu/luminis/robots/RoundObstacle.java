package eu.luminis.robots;

import eu.luminis.entities.Position;
import eu.luminis.entities.World;

public class RoundObstacle extends Obstacle {
    public RoundObstacle(World world, Position position) {
        super(world, position);
    }

    @Override
    public double getSize() {
        return 0;
    }
}
