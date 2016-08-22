package general_temp;

import entities.Plant;
import entities.Position;
import entities.World;
import genetics.Genome;
import util.Range;

import java.util.ArrayList;
import java.util.List;

public class FoodSupply {

	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private double border= 2;
	private World world;
	
	public Option populationSize = new Option(10*8);
	
	public List<Plant> plants = new ArrayList<>();
	
	
	public FoodSupply(World world){
		this.world = world;
		
		this.minX = border;
		this.maxX = world.width - border;
		this.minY = border;
		this.maxY = world.height - border;
		
		for (int i = 0; i < populationSize.get(); i++) {
            this.plants.add(createPlant());
        }
//      this.entityRunNotifier = new Subject();
	}


	
	public void run(){
        for (int i=0; i < this.plants.size(); i++) {
            Plant plant = this.plants.get(i);

            // Replace the food if it's outside canvas boundaries
            if ( plant.position.x < 0 || plant.position.x > world.width || plant.position.y < 0 || plant.position.y > world.height ) {
                plant = this.plants.set(i, createPlant());
            }

            // Replace the food if it's exhausted
            if (!plant.lives()) {
//            	System.out.println("Dead plant");
                plant = this.plants.set(i, createPlant());
            }

            plant.run();
//            this.entityRunNotifier.notify(plant);
        }
    }
	
	public Plant createPlant() {
		Position p = new Position(new Range(minX, maxX).random(), new Range(minY, maxY).random());
		return new Plant(new Genome(0,0), p, world);
    }
}