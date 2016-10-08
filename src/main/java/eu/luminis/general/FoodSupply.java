package eu.luminis.general;

import eu.luminis.entities.Plant;
import eu.luminis.entities.Position;
import eu.luminis.entities.World;
import eu.luminis.genetics.Genome;
import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.List;

public class FoodSupply {
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private double border= 2;
	private World world;
	
	private Option populationSize = new Option(10*8);
	private List<Plant> plants = new ArrayList<>();

	public FoodSupply(World world){
		this.world = world;
		this.minX = border;
		this.maxX = world.getWidth() - border;
		this.minY = border;
		this.maxY = world.getHeight() - border;
		
		for (int i = 0; i < populationSize.get(); i++) {
            this.plants.add(createPlant());
        }
	}

	public void run(){
        for (int i=0; i < this.plants.size(); i++) {
            Plant plant = this.plants.get(i);

            // Replace the food if it's outside canvas boundaries
            if ( plant.position.x < 0 || plant.position.x > world.getWidth() || plant.position.y < 0 || plant.position.y > world.getHeight()) {
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
		Position p = new Position(new Range(minX, maxX).random(), new Range(minY, maxY).random());
		return new Plant(new Genome(0,0), p, world);
    }

	public List<Plant> getPlants() {
		return plants;
	}
}