package eu.luminis.general;

import eu.luminis.entities.Plant;
import eu.luminis.entities.Position;
import eu.luminis.entities.World;
import eu.luminis.genetics.Genome;
import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.List;

public class FoodSupply {
    private Range rangeX;
    private Range rangeY;
	private World world;

	private List<Plant> plants = new ArrayList<>();

	public FoodSupply(World world){
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
            Plant plant = this.plants.get(i);

            // Replace the food if it's outside canvas boundaries
            if (plant.getPosition().x < 0 ||
					plant.getPosition().x > world.getWidth() ||
					plant.getPosition().y < 0 ||
					plant.getPosition().y > world.getHeight()) {
                plant = this.plants.set(i, createPlant());
            }

            // Replace the food if it's exhausted
            if (!plant.lives()) {
                plant = this.plants.set(i, createPlant());
            }

            plant.run();
        }
    }
	
	public Plant createPlant() {
		Position position = new Position(rangeX.random(), rangeY.random());
		return new Plant(new Genome(0,0), position, world);
    }

	public List<Plant> getPlants() {
		return plants;
	}
}