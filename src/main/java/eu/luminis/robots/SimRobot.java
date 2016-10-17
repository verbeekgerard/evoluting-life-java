package eu.luminis.robots;

import eu.luminis.brains.Brain;
import eu.luminis.entities.*;
import eu.luminis.general.CostCalculator;
import eu.luminis.general.EventType;
import eu.luminis.general.Options;
import eu.luminis.genetics.BrainGene;
import eu.luminis.genetics.Genome;

public class SimRobot extends Obstacle {
    private CostCalculator costCalculator = CostCalculator.getInstance();

    private final Robot robot;
    private final SimMotorsController motorsController;
    private final SimSensorController sensorController;
    private final SimServoController servoController;

    private TravelledDistanceRecorder distanceRecorder;

    private double initialEnergy;
    private double movementCost = 0;
    private double collisionDamage = 0;

    private double size;
    private boolean isColliding = false;

    public SimRobot(Genome genome, Position position, SimWorld world) {
        super(world, position);

        BrainGene brainGene = genome.getBrain();
        motorsController = new SimMotorsController(this, genome.getMovement().getLinearForce());
        sensorController = new SimSensorController(this, genome.getSensor().getViewDistance());
        servoController = new SimServoController(this);
        robot = new Robot(
                new Brain(brainGene),
                motorsController,
                servoController,
                sensorController
        );

        this.initialEnergy = Options.initialEnergyOption.get();
        this.distanceRecorder = new TravelledDistanceRecorder(position);
        this.size = Options.sizeOption.get();
    }

    public void run() {
        sensorController.prepareForNearbyObstacles();
        robot.run();
        isColliding = sensorController.isColliding();
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

    public void recordCollision() {
        double velocity = motorsController.getVelocity();
        this.collisionDamage += costCalculator.collide(velocity);
        eventBroadcaster.broadcast(EventType.COLLIDE, collisionDamage);
    }

    private double getDistanceReward() {
        return costCalculator.distanceReward(distanceRecorder.getTotalDistance());
    }

    public double getRadians() {
        return servoController.getAngle() ;
    }
}
