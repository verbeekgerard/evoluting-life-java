package eu.luminis.robots;

import java.util.ArrayList;
import java.util.List;

import eu.luminis.entities.Position;
import eu.luminis.general.Options;
import eu.luminis.genetics.Genome;
import eu.luminis.util.Range;

public class StationaryObstaclePopulation {
	
	private Range rangeX;
    private Range rangeY;
	private SimWorld world;
	
	private List<RoundObstacle> plants = new ArrayList<>();
	
	public StationaryObstaclePopulation(SimWorld world){
		this.world = world;
		double border = 2;

        rangeX = new Range(border, world.getWidth() - border);
        rangeY = new Range(border, world.getHeight() - border);

		int populationSize = (int)Options.plantPopulationSize.get();
		for (int i = 0; i < populationSize; i++) {
            this.plants.add(createPlant());
        }
	}
	
	public void run(){
        for (int i=0; i < this.plants.size(); i++) {
        	RoundObstacle plant = this.plants.get(i);

            // Replace the food if it's outside canvas boundaries
            if (plant.getPosition().x < 0 ||
					plant.getPosition().x > world.getWidth() ||
					plant.getPosition().y < 0 ||
					plant.getPosition().y > world.getHeight()) {
                plant = this.plants.set(i, createPlant());
            }

        }
    }
	
	public RoundObstacle createPlant() {
		Position position = new Position(rangeX.random(), rangeY.random());
		return new RoundObstacle(new Genome(0,0), position, world);
    }
	
    public List<RoundObstacle> getAllRoundObstacles() {
    	return plants;
    }
    
}
