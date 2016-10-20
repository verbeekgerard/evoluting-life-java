package eu.luminis.robots.sim;

import eu.luminis.entities.Position;
import eu.luminis.genetics.LifeGene;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class RoundSimObstacle extends SimObstacle {
    public RoundSimObstacle(SimWorld world, Position position) {
        super(world, position, new LifeGene());
    }

    @Override
    public double getSize() {
        throw new NotImplementedException();
    }

    @Override
    protected void run() {
        // TODO: Change the size from time to time
        throw new NotImplementedException();
    }
}
