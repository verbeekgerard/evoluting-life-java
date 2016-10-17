package eu.luminis.entities;

import java.util.List;

import eu.luminis.brains.Brain;
import eu.luminis.general.*;
import eu.luminis.genetics.Genome;
import eu.luminis.robots.Obstacle;
import eu.luminis.sensors.Eyes;
import eu.luminis.sensors.ObstacleVector;
import eu.luminis.sensors.Obstacles;

public class Animal extends Organism implements Comparable<Animal> {
	private CostCalculator costCalculator;

	private double size;
    private SensorFilter sensorFilter;
	private Eyes eyes;

	private double linearFriction;
	private double velocityLeft = 0;
	private double velocityRight = 0;

	private double linearForce;

    private TravelledDistanceRecorder distanceRecorder;
    private CollisionDetector collisionDetector = new CollisionDetector();

	private double initialEnergy;
	private double collisionDamage = 0;
	private double usedEnergy = 0;
    private boolean isColliding = false;

	private Brain brain;

    public Animal(Genome genome, Position position, World world) {
        super(genome, position, world);

        this.distanceRecorder = new TravelledDistanceRecorder(position);
        this.costCalculator = CostCalculator.getInstance();

        this.size = Options.sizeOption.get();
        this.initialEnergy = Options.initialEnergyOption.get();
        this.linearFriction = Options.linearFrictionOption.get();

        this.sensorFilter = new SensorFilter(this, genome.getSensor().getViewDistance());
        this.eyes = new Eyes(this, genome.getSensor(), world);

        this.linearForce = genome.getMovement().getLinearForce();

        this.brain = new Brain(genome.getBrain());
    }

    @Override
    public double getHealth() {
        return this.initialEnergy + this.getTravelledDistanceReward() - this.collisionDamage - this.usedEnergy;
    }

	@Override
	public double getSize() {
		return this.size * (1 + 0.75 * healthN());
	}

    public Double fitness() {
        return this.getHealth();
    }

    public double getTravelledDistance() {
        return this.distanceRecorder.getTotalDistance();
    }

    public Eyes getEyes() {
        return eyes;
    }

    public boolean isColliding() {
        return isColliding;
    }

    public void run(List<Plant> plants, List<Animal> animals) {
        this.age++;

        turnHead();
        List<Obstacle> filteredOrganisms = this.sensorFilter.filter(plants, animals);
        Obstacles obstacles = eyes.sense(filteredOrganisms);

        AnimalBrainOutput brainOutput = think(obstacles);
        move(brainOutput);

        this.isColliding = collidesWithAny(filteredOrganisms);

        // Register the cost of the cycle
        this.usedEnergy += this.costCalculator.cycle();
    }

    @Override
    public int compareTo(Animal otherAnimal) {
        return otherAnimal.fitness().compareTo(this.fitness());
    }

    protected void turnHead() {
        eyes.turnHead();
        this.usedEnergy +=  this.costCalculator.turnHead();
    }

    protected void move(AnimalBrainOutput brainOutput) {
        if (brainOutput == null) return;

        Position p = this.getPosition();

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

    private AnimalBrainOutput think(Obstacles obstacles) {
        ObstacleVector obstacle = obstacles.getClosestObstacleVector();

        AnimalBrainInput brainInput = new AnimalBrainInput(obstacle, obstacles.getWallDistance(), eyes.getFieldOfView(), eyes.getViewDistance());
        List<Double> inputs = brainInput.getInputs();
        List<Double> thoughtOutput = this.brain.think(inputs);

        return new AnimalBrainOutput(thoughtOutput);
    }

    private boolean collidesWith(Obstacle organism) {
        boolean colliding = collisionDetector.colliding(this, organism);
        if (!colliding) return false;

		Position p = this.getPosition();
        double v = this.getVelocity();

		double dx = Math.cos(p.a) * v;
		double dy = Math.sin(p.a) * v;

		// Move the entity opposite to it's velocity
		p.x -= dx;
		p.y -= dy;

		// Increase eu.luminis.entities total collision counter
		this.collisionDamage += this.costCalculator.collide(v);

		// Increment global collision counter
        eventBroadcaster.broadcast(EventType.COLLIDE, collisionDamage);
        return true;
	}

	private boolean collidesWithAny(List<Obstacle> organisms) {
        boolean isColliding = false;

        for (Obstacle organism : organisms) {
            isColliding = isColliding || collidesWith(organism);
        }

        return isColliding;
	}

	private double getVelocity() {
		return (this.velocityRight + this.velocityLeft) / 2;
	}

	private double getTravelledDistanceReward() {
        return this.costCalculator.distanceReward(getTravelledDistance());
    }

    private double healthN() {
        double health = this.getHealth();
        return health > 0 ? 1 - 1 / Math.exp(health / 200) : 0;
    }
}