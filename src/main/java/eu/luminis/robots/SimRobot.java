package eu.luminis.robots;

import eu.luminis.brains.Brain;
import eu.luminis.entities.*;
import eu.luminis.general.CostCalculator;
import eu.luminis.general.EventType;
import eu.luminis.general.Options;
import eu.luminis.genetics.BrainGene;
import eu.luminis.genetics.Genome;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class SimRobot extends Obstacle {
    private CostCalculator costCalculator = CostCalculator.getInstance();

    private final Robot robot;
    private final SimMotorsController motorsController;

    private TravelledDistanceRecorder distanceRecorder;
    private CollisionDetector collisionDetector = new CollisionDetector();
    private SensorFilter sensorFilter;

    private double initialEnergy;
    private double movementCost = 0;
    private double collisionDamage = 0;

    private double size;
    private boolean isColliding = false;

    public SimRobot(Genome genome, Position position, World world) {
        super(world, position);

        BrainGene brainGene = genome.getBrain();
        motorsController = new SimMotorsController(this, genome.getMovement().getLinearForce());
        this.robot = new Robot(
                new Brain(brainGene),
                motorsController,
                new SimServoController(this),
                new SimSensor(this)
        );

        this.initialEnergy = Options.initialEnergyOption.get();
        this.distanceRecorder = new TravelledDistanceRecorder(position);
        this.size = Options.sizeOption.get();
        this.sensorFilter = new SensorFilter(this, genome.getSensor().getViewDistance());
    }

    public void run() {
        this.robot.run();
    }

    public Double fitness() {
        return this.initialEnergy + getDistanceReward() - collisionDamage - movementCost;
    }

    public boolean isColliding() {
        return isColliding;
    }

    @Override
    public double getSize() {
        double fitness = fitness();
        double fitnessN = fitness > 0 ? 1 - 1 / Math.exp(fitness / 200) : 0;

        return this.size * (1 + 0.75 * fitnessN);
    }

    public void recordMove(Position newPosition, double acceleration) {
        distanceRecorder.recordMove(newPosition);
        movementCost += costCalculator.accelerate(acceleration);
    }

    private boolean collidesWithAny(List<Obstacle> obstacles) {
        boolean isColliding = false;

        for (Obstacle obstacle : obstacles) {
            isColliding = isColliding || collidesWith(obstacle);
        }

        return isColliding;
    }

    private boolean collidesWith(Obstacle obstacle) {
        boolean colliding = collisionDetector.colliding(this, obstacle);
        if (!colliding) return false;

        Position position = this.getPosition();
        double velocity = motorsController.getVelocity();

        double dx = Math.cos(position.a) * velocity;
        double dy = Math.sin(position.a) * velocity;

        // Move the entity opposite to it's velocity
        position.x -= dx;
        position.y -= dy;

        this.collisionDamage += costCalculator.collide(velocity);

        // Increment global collision counter
        eventBroadcaster.broadcast(EventType.COLLIDE, collisionDamage);
        return true;
    }

    private double getDistanceReward() {
        return costCalculator.distanceReward(distanceRecorder.getTotalDistance());
    }
}
