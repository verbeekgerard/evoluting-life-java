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
import util.Range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Animal extends Organism implements Comparable<Animal> {
	public Position startPosition;

	public int leftAccelerate = 0;
	public int leftDecelerate = 0;
	public int rightAccelerate = 0;
	public int rightDecelerate = 0;

	public double size;
	public double collided = 0;
	public double usedEnergy = 0;
	public Eyes eyes;

	public double initialEnergy;
	public double linearFriction;
	public double angularFriction;
	public double velocityLeft = 0;
	public double velocityRight = 0;

	public double linearForce;

	private int steps = 0;
	private double historicalDistance = 0;
	private double currentDistance = 0;

	private Brain brain;
	private Map<String, Double> output;

	@Override
	public double getSize() {
		return Options.sizeOption.get() * (1 + 0.75 * healthN());
	}

	public Map<String, Double> getInitialOutput() {
		Map<String, Double> initialOutput = new HashMap<>();
		initialOutput.put("leftAccelerate", 0d);
		initialOutput.put("leftDecelerate", 0d);
		initialOutput.put("rightAccelerate", 0d);
		initialOutput.put("rightDecelerate", 0d);
		return initialOutput;
	}

	public Animal(Genome genome, Position position, World world) {
		super(genome, position, world);
		initializeDistanceVariables();

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
		// random
		inputs.add(new Range(0, 1).random());

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
		if (organism == this)
			return;

		Position p = this.position;

		// Use formula for a circle to find food
		double x2 = (p.x - organism.position.x); x2 *= x2;
		double y2 = (p.y - organism.position.y); y2 *= y2;
		double s2 = organism.getSize() + 2; s2 *= s2;

		// Only if we are within the circle, collide it
		if (x2 + y2 >= s2) {
			return;
		}

		double dx = (Math.cos(p.a) * this.getVelocity());
		double dy = (Math.sin(p.a) * this.getVelocity());

		// Move the entity
		p.x -= dx;
		p.y -= dy;

		// Increase entities total collision counter
		this.collided += CostCalculator.collide(this.getVelocity());

		// Increment global collision counter
		Main.getInstance().broadcast(EventType.COLLIDE, collided);
	}

	public void collide(List<Plant> plants, List<Animal> animals) {
		// TODO: optimize this by combining it with the sense action
		for (int i = 0; i < plants.size(); i++) {
			checkColission(plants.get(i));
		}
		for (int i = 0; i < animals.size(); i++) {
			checkColission(animals.get(i));
		}
	}

	public double getVelocity() {
		return (this.velocityRight + this.velocityLeft) / 2;
	}
	
	public double getTravelledDistance() {
		return this.historicalDistance + this.currentDistance;
	}

	public void move() {
		Position p = this.position;

		// Keep angles within bounds
		p.a = p.a % (Math.PI * 2);
		if (p.a < 0)
			p.a += Math.PI * 2;

		// F=m*a => a=F/m, dv=a*dt => dv=dt*F/m, dt=one cycle, m=1
		double accelerationLeft = (this.output.get("leftAccelerate") - this.output.get("leftDecelerate"))
				* this.linearForce;
		this.velocityLeft += accelerationLeft;
		this.velocityLeft -= this.velocityLeft * Options.linearFrictionOption.get();

		double accelerationRight = (this.output.get("rightAccelerate") - this.output.get("rightDecelerate"))
				* this.linearForce;
		this.velocityRight += accelerationRight;
		this.velocityRight -= this.velocityRight * Options.linearFrictionOption.get();

		p.a += (this.velocityLeft - this.velocityRight) / 10;

		// Convert movement vector into polar
		double dx = (Math.cos(p.a) * this.getVelocity());
		double dy = (Math.sin(p.a) * this.getVelocity());

		// Move the entity
		p.x += dx;
		p.y += dy;

		// Reward for traveled distance. Maybe redefine the traveled distance.
		this.currentDistance = CostCalculator.travelledDistance(this.position.calculateDistance(this.startPosition));
		this.steps++;
		
		if (steps > 300 || this.currentDistance > 300) {
			initializeDistanceVariables();
		}

		// Register the cost of the forces applied for acceleration
		this.usedEnergy += CostCalculator.rotate(p.a);
		this.usedEnergy += CostCalculator.accelerate((accelerationLeft + accelerationRight));
	}

	@Override
	public double getHealth() {
		return this.initialEnergy + this.getTravelledDistance() - this.collided - this.usedEnergy;
	}

	public void run(List<Plant> plants, List<Animal> animals) {
		this.age++;

		Targets targets = this.eyes.sense(plants, animals);
		think(targets);
		move();
		collide(plants, animals);

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
		for (int i = 0; i < thoughtOutput.size(); i++) {
			this.output.put(keys.get(i), thoughtOutput.get(i));
		}
	}

	@Override
	public int compareTo(Animal otherAnimal) {
		return otherAnimal.rank().compareTo(this.rank());
	}
	
	private void initializeDistanceVariables() {
		this.historicalDistance += this.currentDistance;
		this.steps = 0;
		this.currentDistance = 0;
		this.startPosition = new Position(position);
	}
}