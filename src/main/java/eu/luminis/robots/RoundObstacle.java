package eu.luminis.robots;

import eu.luminis.entities.Position;
import eu.luminis.general.Options;
import eu.luminis.genetics.Genome;
import eu.luminis.util.Range;

public class RoundObstacle extends Obstacle {

	private double size;
	
    public RoundObstacle(SimWorld world, Position position) {
        super(world, position);
    }
    
    public RoundObstacle(Genome genome, Position position, SimWorld world) {
    	super(world, position);
		
	    size = new Range(Options.minFoodSize.get(), Options.maxFoodSize.get()).random();
    }

	@Override
	public double getSize() {
		return size;
	};
}
