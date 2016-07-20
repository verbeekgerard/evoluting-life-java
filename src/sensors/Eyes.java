package sensors;

import java.util.ArrayList;
import java.util.List;

import entities.Animal;
import entities.Organism;
import entities.Plant;
import entities.Position;
import entities.World;
import genetics.SensorGene;

public class Eyes {

	public Animal owner;
	public double viewDistance;
	public double fieldOfView;
	public World world;
	
	public Eyes(Animal owner, SensorGene sensorGen, World world){
		this.owner = owner;
		
		this.viewDistance = sensorGen.viewDistance;
      	this.fieldOfView = sensorGen.fieldOfView;
      	this.world = world;
	}
	
	public Position getPosition() {
        return owner.position;
	}
	
	public Double wallDistance() {
       
        Position p = this.getPosition();
//        var adj, angle, hyp;

        // If not near to a wall
        if (p.x > this.viewDistance &&
          world.width - p.x > this.viewDistance &&
          p.y > this.viewDistance &&
          world.height - p.y > this.viewDistance) {
          return this.viewDistance;
        }

        // Keep angles within bounds
        p.a = p.a % (Math.PI * 2);
        if (p.a < 0) {
          p.a += Math.PI * 2;
        }

        // cosine(angle) = adjacent / hypotenuse
        // hypotenuse = adjacent / cosine(angle)

        // sine(angle) = opposite / hypotenuse
        // hypotenuse = opposite / sine(angle)

        double distX;
        double distY;
        if (p.a >= 0 && p.a <= Math.PI * 0.5) {
          // Facing right wall, bottom wall: x=world.width, y=world.height
          distX = (world.width - p.x) / Math.cos(p.a);
          distY = (world.height - p.y) / Math.sin(p.a);

          return Math.min(Math.min(distX, distY), this.viewDistance);
        }

        if (p.a >= Math.PI * 0.5 && p.a <= Math.PI) {
          // Facing left wall, bottom wall: x=0, y=world.height
          distX = (0 - p.x) / Math.cos(p.a);
          distY = (world.height - p.y) / Math.sin(p.a);

          return Math.min(Math.min(distX, distY), this.viewDistance);
        }

        if (p.a >= Math.PI && p.a <= Math.PI * 1.5) {
          // Facing left wall, top wall: x=0, y=0
          distX = (0 - p.x) / Math.cos(p.a);
          distY = (0 - p.y) / Math.sin(p.a);

          return Math.min(Math.min(distX, distY), this.viewDistance);
        }

        if (p.a >= Math.PI * 1.5 && p.a < Math.PI * 2) {
          // Facing right wall, top wall: x=world.width, y=0
          distX = (world.width - p.x) / Math.cos(p.a);
          distY = (0 - p.y) / Math.sin(p.a);

          return Math.min(Math.min(distX, distY), this.viewDistance);
        }
		return null;
	}
	
	 public List<FoodVector> findOrganisms(List<? extends Organism> organisms) {
  
        Position p = this.getPosition();

        // An array of vectors to foods from this entity's perspective
        List<FoodVector> foodVectors = new ArrayList<>();

        // Loop through foodSupply
        for (int i = 0; i < organisms.size(); i++) {
          Organism organism = organisms.get(i);

          if (organism == this.owner) continue;

          // Find polar coordinates of food relative this entity
          double dx = organism.position.x - p.x;
          double dy = organism.position.y - p.y;

          // Check bounding box first for performance
          if (Math.abs(dx) > this.viewDistance || Math.abs(dy) > this.viewDistance) continue;

          // Find angle of food relative to entity
          if (dx == 0) dx = 0.000000000001;
          double angle = p.a - Math.atan2(dy, dx);

          // Convert angles to right of center into negative values
          if (angle > Math.PI) angle -= 2 * Math.PI;

          // Calculate distance to this food
          double distance = Math.sqrt(dx * dx + dy * dy);

          // If the food is outside the viewing range, skip it
          if (Math.abs(angle) > this.fieldOfView / 2 || distance > this.viewDistance) continue;

          foodVectors.add(new FoodVector(distance, angle, organism));

        }

        // Sort our food vectors by distance
//        return foodVectors.sort(sortFoodVectorsCallback); // TODO
        return foodVectors;
      }
	 
	 public Targets sense(List<Plant> plants, List<Animal> animals) {
		 
		 findOrganisms(animals);
		 Targets targets = new Targets();
		 targets.plants = findOrganisms(plants);
		 targets.animals = findOrganisms(animals);
		 targets.wallDistance = wallDistance();
		 
//          return {
//            plants: findOrganisms.call(this, plants || []),
//            animals: findOrganisms.call(this, animals || []),
//            wall: {
//              distance: wallDistance.call(this)
//            }
//          }
		 return targets;
      }
	
}

//		      function sortFoodVectorsCallback(a, b) {
//		        return a.distance - b.distance;
//		      }

		     