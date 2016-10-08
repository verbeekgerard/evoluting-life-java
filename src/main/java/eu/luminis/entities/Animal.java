package eu.luminis.entities;

import java.util.List;

import eu.luminis.brains.Brain;
import eu.luminis.general.CostCalculator;
import eu.luminis.general.EventType;
import eu.luminis.general.General;
import eu.luminis.general.Options;
import eu.luminis.genetics.Genome;
import eu.luminis.sensors.Eyes;
import eu.luminis.sensors.ObstacleVector;
import eu.luminis.sensors.Targets;

public class Animal extends Organism implements Comparable<Animal> {
	private CostCalculator costCalculator;

	private double size;
	private Eyes eyes;

	private double linearFriction;
	private double velocityLeft = 0;
	private double velocityRight = 0;

	private double linearForce;

    private TravelledDistanceRecorder distanceRecorder;

	private double initialEnergy;
	private double collided = 0;
	private double usedEnergy = 0;

	private Brain brain;

	@Override
	public double getSize() {
		return this.size * (1 + 0.75 * healthN());
	}

	public Animal(Genome genome, Position position, World world) {
		super(genome, position, world);

        this.distanceRecorder = new TravelledDistanceRecorder(position);
		this.costCalculator = CostCalculator.getInstance();

		this.size = Options.sizeOption.get();
		this.initialEnergy = Options.initialEnergyOption.get();
        this.linearFriction = Options.linearFrictionOption.get();

		this.eyes = new Eyes(this, genome.sensor, world);

		this.linearForce = genome.movement.linearForce;

		this.brain = new Brain(genome.brain);
	}

	public Double fitness() {
		return this.getHealth();
	}

	public double healthN() {
		double health = this.getHealth();
		return health > 0 ? 1 - 1 / Math.exp(health / 200) : 0;
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

		// Increase eu.luminis.entities total collision counter
		this.collided += this.costCalculator.collide(this.getVelocity());

		// Increment global collision counter
		General.getInstance().broadcast(EventType.COLLIDE, collided);
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
		return this.distanceRecorder.getTotalDistance();
	}

	public double getTravelledDistanceReward() {
        return this.costCalculator.distanceReward(getTravelledDistance());
    }

	public void move(AnimalBrainOutput brainOutput) {
        if (brainOutput == null) return;

		Position p = this.position;

		// Keep angles within bounds
		p.a = p.a % (Math.PI * 2);
		if (p.a < 0)
			p.a += Math.PI * 2;

		// F=m*a => a=F/m, dv=a*dt => dv=dt*F/m, dt=one cycle, m=1
		double accelerationLeft = brainOutput.getAccelerationLeft() * this.linearForce;
		this.velocityLeft += accelerationLeft;
		this.velocityLeft -= this.velocityLeft * linearFriction;

		double accelerationRight = brainOutput.getAccelerationRight() * this.linearForce;
		this.velocityRight += accelerationRight;
		this.velocityRight -= this.velocityRight * linearFriction;

		p.a += (this.velocityLeft - this.velocityRight) / 10;

		// Convert movement vector into polar
		double dx = (Math.cos(p.a) * this.getVelocity());
		double dy = (Math.sin(p.a) * this.getVelocity());

		// Move the entity
		p.x += dx;
		p.y += dy;

        this.distanceRecorder.recordMove(p);

		// Register the cost of the forces applied for acceleration
		this.usedEnergy += this.costCalculator.accelerate((accelerationLeft + accelerationRight));
	}

	@Override
	public double getHealth() {
		return this.initialEnergy + this.getTravelledDistanceReward() - this.collided - this.usedEnergy;
	}

	public void run(List<Plant> plants, List<Animal> animals) {
		this.age++;

		Targets targets = eyes.sense(plants, animals);
        AnimalBrainOutput brainOutput = think(targets);
		move(brainOutput);
		collide(plants, animals);

		// Register the cost of the cycle
		this.usedEnergy += this.costCalculator.cycle();
	}

	public AnimalBrainOutput think(Targets targets) {
		ObstacleVector obstacle = null;

		if (targets.obstacles.size() > 0) {
			obstacle = targets.obstacles.get(0);
		}

        AnimalBrainInput brainInput = new AnimalBrainInput(obstacle, targets.wallDistance, eyes.fieldOfView, eyes.viewDistance);
		List<Double> inputs = brainInput.getInputs();
        List<Double> thoughtOutput = this.brain.think(inputs);

        return new AnimalBrainOutput(thoughtOutput);
	}

	@Override
	public int compareTo(Animal otherAnimal) {
		return otherAnimal.fitness().compareTo(this.fitness());
	}
	
	public Eyes getEyes() {
		return eyes;
	}
}