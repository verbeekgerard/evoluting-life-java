package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import brains.Brain;
import genetics.Genome;
import main.CostCalculator;
import main.Main;
import main.EventType;
import main.Options;
import sensors.Eyes;
import sensors.FoodVector;
import sensors.Targets;

public class Animal extends Organism implements Comparable<Animal> {

	public int turnLeft = 0;    // Left angle
	public int turnRight = 0;    // Right angle
	public int accelerate = 0;// Velocity accelerator
	public int decelerate = 0; // Velocity suppressor
	public int eat = 0; 
	public double size;
	public double consumed;
	public double hunger;
	public Eyes eyes;
	
	
	public double initialEnergy;
	public double linearFriction;
	public double angularFriction;
	public double linearVelocity;
	public double angularVelocity;
	
	public double linearForce;
	public double angularForce;
	
    private Brain brain;
	private Map<String, Double> output;
    
    @Override
    public double getSize() {
        return Options.sizeOption.get() * (1 + 0.75 * healthN());
    }
    
    @Override
    public double consume() {
        double size = getHealth();
        hunger++;
        // static value until nutricion becomes available
        Main.getInstance().broadcast(EventType.CONSUMED, 1);
        
        return size < 0 ? 0 : size < 1 ? size : 1;
    }
    
    public Map<String, Double> getInitialOutput(){
    	Map<String, Double> initialOutput = new HashMap<>();
    	initialOutput.put("turnLeft", 0d);
    	initialOutput.put("turnRight", 0d);
    	initialOutput.put("accelerate", 0d);
    	initialOutput.put("decelerate", 0d);
    	initialOutput.put("eat", 0d);
    	return initialOutput;
    }

	public Animal(Genome genome, Position position, World world) {
		super(genome, position, world);
		
		this.size = Options.sizeOption.get();
        this.initialEnergy = Options.initialEnergyOption.get();

        this.angularFriction = Options.angularFrictionOption.get();
        this.linearFriction = Options.linearFrictionOption.get();

        this.linearVelocity = 0;
        this.angularVelocity = 0;

        this.eyes = new Eyes(this, genome.sensor, world);

        this.linearForce = genome.movement.linearForce;
        this.angularForce = genome.movement.angularForce;

        this.brain = new Brain(genome.brain);

        this.output = getInitialOutput();

        this.consumed = Options.initialEnergyOption.get();   // Food eaten
        this.hunger = 0;
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
        
        Animal animal = visibleAnimal != null ? (Animal) visibleAnimal.organism : null;
        Plant plant = visibleFood != null ? (Plant) visibleFood.organism : null;

        List<Double> inputs = new ArrayList<>();
        // left
        inputs.add(visibleAnimal != null ? (fieldOfView / 2 + visibleAnimal.angle) / fieldOfView : 0);
        // right
        inputs.add(visibleAnimal != null ? (fieldOfView / 2 - visibleAnimal.angle) / fieldOfView : 0);
        // distance
        inputs.add(visibleAnimal != null ? (viewDistance - visibleAnimal.distance) / viewDistance : 0);
        // food supply
        inputs.add(visibleAnimal != null ? visibleAnimal.organism != null ? animal.healthN() : 0 : 0);
        // left
        inputs.add(visibleFood != null ? (fieldOfView / 2 + visibleFood.angle) / fieldOfView : 0);
        // right
        inputs.add(visibleFood != null ? (fieldOfView / 2 - visibleFood.angle) / fieldOfView : 0);
        // distance
        inputs.add(visibleFood != null ? (viewDistance - visibleFood.distance) / viewDistance : 0);
        // food supply
        inputs.add(visibleFood != null ? visibleFood.organism != null ? plant.healthN() : 0 : 0);
        // negative nutrition
        inputs.add(visibleFood != null ? visibleFood.organism != null ? plant.nutrition < 0 ? plant.getNutritionN() : 0 : 0 : 0);
        // positive nutrition
        inputs.add(visibleFood != null ? visibleFood.organism != null ? plant.nutrition > 0 ? plant.getNutritionN() : 0 : 0 : 0);

        // distance to wall
        inputs.add((viewDistance - wallDistance) / viewDistance);
        // energy
        inputs.add(this.healthN());

      // random
      // Range(0, 1).random()
       
        double normalizationFactor = (Options.maxThreshold.get() + Options.minThreshold.get()) / 2;
        // Normalize inputs
        for (int i = 0; i < inputs.size(); i++) {
        	Double value = inputs.get(i);
        	value *= normalizationFactor;
        	inputs.set(i, value);
        }

        return inputs;
    }
	
	
	public void eatOrganism(Organism organism) {
        /*jshint validthis: true */
        if (organism == null) return;

        // Use formula for a circle to find food
        double x2 = (this.position.x - organism.position.x); x2 *= x2;
        double y2 = (this.position.y - organism.position.y); y2 *= y2;
        double s2 = organism.getSize() + 2; s2 *= s2;

        // If we are within the circle, eat it
        if (x2 + y2 >= s2) {
          return;
        }
        
        // Increase entities total eaten counter
        double consumed = organism.consume();
        consumed += consumed;

        // Increment global eaten counter
        Main.getInstance().broadcast(EventType.EAT, consumed);
    }
	
	public void eat(Targets targets) {
        if (this.output.get("eat") == null) return;

        if (targets.plants.size() > 0){
        	eatOrganism(targets.plants.get(0).organism);
        } 
        if (targets.animals.size() > 0){
        	eatOrganism(targets.animals.get(0).organism);
        } 
    }
	
	public void move() {

        Position p = this.position;

        double angularAcceleration = (this.output.get("turnLeft") - this.output.get("turnRight")) * this.angularForce;
        this.angularVelocity += angularAcceleration;
        this.angularVelocity -= this.angularVelocity * Options.angularFrictionOption.get();
        p.a += this.angularVelocity;

        // Keep angles within bounds
        p.a = p.a % (Math.PI * 2);
        if (p.a < 0) p.a += Math.PI * 2;

        // F=m*a => a=F/m, dv=a*dt => dv=dt*F/m, dt=one cycle, m=1
        double linearAcceleration = (this.output.get("accelerate") - this.output.get("decelerate")) * this.linearForce;
        this.linearVelocity += linearAcceleration;
        this.linearVelocity -= this.linearVelocity * Options.linearFrictionOption.get();

        // Convert movement vector into polar
        double dx = (Math.cos(p.a) * this.linearVelocity);
        double dy = (Math.sin(p.a) * this.linearVelocity);

        // Move the entity
        p.x += dx;
        p.y += dy;
        
        // Register the cost of the forces applied for acceleration
        this.hunger += CostCalculator.rotate(angularAcceleration * getHealth());
        this.hunger += CostCalculator.accelerate(linearAcceleration * getHealth());
    }
      
    @Override
	public double getHealth() {
		return this.consumed - this.hunger;
	}
    
	public void run(List<Plant>plants, List<Animal> animals){
		
		this.age++;

		Targets targets = this.eyes.sense(plants, animals);

        think(targets);
        eat(targets);
        move();

        // Register the cost of the cycle
        this.hunger += CostCalculator.cycle();
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
		keys.add("turnLeft");
		keys.add("turnRight");
		keys.add("accelerate");
		keys.add("decelerate");
		keys.add("eat");
		
		// TODO cant we loop through the input list?
		
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

