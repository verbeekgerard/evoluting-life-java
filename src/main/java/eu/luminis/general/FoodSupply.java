package eu.luminis.general;

import eu.luminis.entities.Plant;
import eu.luminis.entities.Position;
import eu.luminis.entities.World;
import eu.luminis.genetics.Genome;
import eu.luminis.robots.sim.BorderDimensionsPositionGenerator;

import java.util.ArrayList;
import java.util.List;

public class FoodSupply {
    private final BorderDimensionsPositionGenerator borderDimensionsPositionGenerator;
	private World world;

	private List<Plant> plants = new ArrayList<>();

	public FoodSupply(World world){
        borderDimensionsPositionGenerator = new BorderDimensionsPositionGenerator(world);
        this.world = world;

        int populationSize = (int)Options.roundObstaclePopulationSize.get();
		for (int i = 0; i < populationSize; i++) {
            this.plants.add(createPlant());
        }
	}

	public void run(){
        for (int i=0; i < this.plants.size(); i++) {
            Plant plant = this.plants.get(i);

            // Replace the food if it's exhausted
            if (!plant.lives()) {
                plant = this.plants.set(i, createPlant());
            }

            plant.run();
        }
    }
	
	public Plant createPlant() {
		Position position = borderDimensionsPositionGenerator.createRandomPositionWithinFixedBorder(2);
		return new Plant(new Genome(0,0), position, world);
    }

	public List<Plant> getPlants() {
		return plants;
	}
}