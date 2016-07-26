package entities;

import brains.Brain;
import genetics.Genome;
import main.CostCalculator;
import main.EventType;
import main.Main;
import main.Options;
import sensors.Eyes;
import sensors.FoodVector;
import sensors.Targets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Animal extends Organism implements Comparable<Animal> {
	public Position startPosition;
	
	public int leftAccelerate = 0;    // Left angle
	public int leftDecelerate = 0;    // Right angle
	public int rightAccelerate = 0;// Velocity accelerator
	public int rightDecelerate = 0; // Velocity suppressor
	
	public double size;
	public double travelledDistance = 0;
	public double collided = 0;
	public double usedEnergy = 0;
	public Eyes eyes;
	
	public double initialEnergy;
	public double linearFriction;
	public double angularFriction;
	public double velocityLeft = 0;
	public double velocityRight = 0;
	
	public double linearForce;
	
    private Brain brain;
	private Map<String, Double> output;
    
    @Override
    public double getSize() {
        return Options.sizeOption.get() * (1 + 0.75 * healthN());
    }
    
    public Map<String, Double> getInitialOutput(){
    	Map<String, Double> initialOutput = new HashMap<>();
    	initialOutput.put("leftAccelerate", 0d);
    	initialOutput.put("leftDecelerate", 0d);
    	initialOutput.put("rightAccelerate", 0d);
    	initialOutput.put("rightDecelerate", 0d);
    	return initialOutput;
    }

	public Animal(Genome genome, Position position, World world) {
		super(genome, position, world);
		
		this.startPosition = new Position(position);
		this.size = Options.sizeOption.get();
        this.initialEnergy = Options.initialEnergyOption.get();

        this.angularFriction = Options.angularFrictionOption.get();
        this.linearFriction = Options.linearFrictionOption.get();

        this.eyes = new Eyes(this, genome.sensor, world);

        this.linearForce = genome.movement.linearForce;

        this.brain = new Brain(genome.brain);
        this.output = getInitialOutput();
	}
	
	public Double rank() { 
        return this.getHealth();
    }

	
	public double healthN() {
        double health = this.getHealth();
        return health > 0 ? 1 - 1 / Math.exp(health / 200) : 0;
	}
	
	public List<Double> createInput(FoodVector visibleFood, FoodVector visibleAnimal, double wallDistance) {
   
        double fieldOfView = eyes.fieldOfView;
        double viewDistance = eyes.viewDistance;

        List<Double> inputs = new ArrayList<>();
        
        // left
        inputs.add(visibleAnimal != null ? (fieldOfView / 2 + visibleAnimal.angle) / fieldOfView : 0);
        // right
        inputs.add(visibleAnimal != null ? (fieldOfView / 2 - visibleAnimal.angle) / fieldOfView : 0);
        // distance
        inputs.add(visibleAnimal != null ? (viewDistance - visibleAnimal.distance) / viewDistance : 0);

        // left
        inputs.add(visibleFood != null ? (fieldOfView / 2 + visibleFood.angle) / fieldOfView : 0);
        // right
        inputs.add(visibleFood != null ? (fieldOfView / 2 - visibleFood.angle) / fieldOfView : 0);
        // distance
        inputs.add(visibleFood != null ? (viewDistance - visibleFood.distance) / viewDistance : 0);

        // distance to wall
        inputs.add((viewDistance - wallDistance) / viewDistance);
        // energy
        inputs.add(this.healthN());

        double normalizationFactor = (Options.maxThreshold.get() + Options.minThreshold.get()) / 2;
        // Normalize inputs
        for (int i = 0; i < inputs.size(); i++) {
        	Double value = inputs.get(i);
        	value *= normalizationFactor;
        	inputs.set(i, value);
        }

        return inputs;
    }
	
	public void checkColission(Organism organism) {
        if (organism == null) return;

        // Use formula for a circle to find food
        double x2 = (this.position.x - organism.position.x); x2 *= x2;
        double y2 = (this.position.y - organism.position.y); y2 *= y2;
        double s2 = organism.getSize() + 2; s2 *= s2;

        // Only if we are within the circle, collide it
        if (x2 + y2 >= s2) {
          return;
        }
        
        // Increase entities total collision counter
        this.collided += CostCalculator.collide(1);

        // Increment global collision counter
        Main.getInstance().broadcast(EventType.COLLIDE, collided);
    }
	
	public void collide(Targets targets) {
        if (targets.plants.size() > 0){
        	checkColission(targets.plants.get(0).organism);
        } 
        if (targets.animals.size() > 0){
        	checkColission(targets.animals.get(0).organism);
        } 
    }
	
	public void move() {

        Position p = this.position;

        // Keep angles within bounds
        p.a = p.a % (Math.PI * 2);
        if (p.a < 0) p.a += Math.PI * 2;

        // F=m*a => a=F/m, dv=a*dt => dv=dt*F/m, dt=one cycle, m=1
        double accelerationLeft = (this.output.get("leftAccelerate") - this.output.get("leftDecelerate")) * this.linearForce;
        this.velocityLeft += accelerationLeft;
        this.velocityLeft -= this.velocityLeft * Options.linearFrictionOption.get();

        double accelerationRight = (this.output.get("rightAccelerate") - this.output.get("rightDecelerate")) * this.linearForce;
        this.velocityRight += accelerationRight;
        this.velocityRight -= this.velocityRight * Options.linearFrictionOption.get();

        p.a += (this.velocityLeft - this.velocityRight)/10;

        // Convert movement vector into polar
        double dx = (Math.cos(p.a) * (this.velocityRight + this.velocityLeft)/2);
        double dy = (Math.sin(p.a) * (this.velocityRight + this.velocityLeft)/2);

        // Move the entity
        p.x += dx;
        p.y += dy;
                
        this.travelledDistance = CostCalculator.travelledDistance(this.position.calculateDistance(this.startPosition));
        
        // Register the cost of the forces applied for acceleration
        this.usedEnergy += CostCalculator.rotate(p.a);
        this.usedEnergy += CostCalculator.accelerate((accelerationLeft + accelerationRight));
    }
      
    @Override
	public double getHealth() {
		return this.initialEnergy +
				this.travelledDistance -
				this.collided - 
				this.usedEnergy;
	}
    
	public void run(List<Plant>plants, List<Animal> animals){
		
		this.age++;

		Targets targets = this.eyes.sense(plants, animals);

        think(targets);
        move();
        collide(targets);

        // Register the cost of the cycle
        this.usedEnergy += CostCalculator.cycle();
	}
	
	public void think(Targets targets) {
		FoodVector plantFoodVector = null;
		FoodVector animalFoodVector = null;
		
		if (targets.plants.size() > 0) {
			plantFoodVector = targets.plants.get(0);
		}
		if (targets.animals.size() > 0) {
			animalFoodVector = targets.animals.get(0);
		}
		
		List<Double> inputs = createInput(plantFoodVector, animalFoodVector, targets.wallDistance);

		List<String> keys = new ArrayList<>();
		keys.add("leftAccelerate");
		keys.add("leftDecelerate");
		keys.add("rightAccelerate");
		keys.add("rightDecelerate");
		
        List<Double> thoughtOutput = this.brain.think(inputs);
        for (int i=0; i<thoughtOutput.size();i++) {
        	this.output.put(keys.get(i), thoughtOutput.get(i));
        }
	}

	@Override
	public int compareTo(Animal otherAnimal) {
		return otherAnimal.rank().compareTo(this.rank());
	}

}