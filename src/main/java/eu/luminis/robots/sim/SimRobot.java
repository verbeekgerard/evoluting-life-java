package eu.luminis.robots.sim;

import eu.luminis.brains.Brain;
import eu.luminis.entities.*;
import eu.luminis.general.CostCalculator;
import eu.luminis.general.EventType;
import eu.luminis.general.Options;
import eu.luminis.genetics.BrainGene;
import eu.luminis.genetics.Genome;
import eu.luminis.robots.core.IAngleRetriever;
import eu.luminis.robots.core.Robot;

public class SimRobot extends SimObstacle implements Comparable<SimRobot> {
    private CostCalculator costCalculator = CostCalculator.getInstance();

    private final Genome genome;
    private final Robot robot;
    private final SimMotorsController motorsController;
    private final SimServoController servoController;
    private final SimSensorController sensorController;

    private TravelledDistanceRecorder distanceRecorder;

    private double initialEnergy;
    private double usedEnergy = 0;
    private double movementCost = 0;
    private double headTurnCost = 0;
    private double collisionDamage = 0;

    private double size;
    private boolean isColliding = false;

    public SimRobot(Genome genome, Position position, SimWorld world) {
        super(world, position, genome.getLife());
        this.genome = genome;
        this.initialEnergy = Options.initialEnergy.get();
        this.size = Options.sizeOption.get();
        this.distanceRecorder = new TravelledDistanceRecorder(position);

        BrainGene brainGene = genome.getBrain();
        motorsController = new SimMotorsController(this, genome.getMovement().getLinearForce());
        servoController = new SimServoController(this, genome.getMovement().getAngularForce());
        sensorController = new SimSensorController(this, genome.getSensor().getViewDistance(), servoController);
        robot = new Robot(
                new Brain(brainGene),
                motorsController,
                servoController,
                sensorController
        );
    }

    public Double fitness() {
        return this.initialEnergy + getDistanceReward() - usedEnergy - collisionDamage - movementCost - headTurnCost;
    }

    public boolean isColliding() {
        return isColliding;
    }

    @Override
    public int compareTo(SimRobot other) {
        return other.fitness().compareTo(this.fitness());
    }

    @Override
    public double getSize() {
        double fitness = fitness();
        double fitnessN = fitness > 0 ? 1 - 1 / Math.exp(fitness / 200) : 0;

        return this.size * (1 + 0.75 * fitnessN);
    }

    public Genome getGenome() {
        return genome;
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

    public void recordAngleChange(double acceleration) {
        headTurnCost += costCalculator.turnHead(acceleration);
    }

    public IAngleRetriever getServo() {
        return servoController;
    }

    public double getTravelledDistance() {
        return distanceRecorder.getTotalDistance();
    }

    @Override
    protected void run() {
        sensorController.prepareForNearbyObstacles();
        robot.run();
        isColliding = sensorController.isColliding();

        usedEnergy += this.costCalculator.cycle();
    }

    @Override
    protected boolean isAlive() {
        return fitness() > 0;
    }

    private double getDistanceReward() {
        return costCalculator.distanceReward(distanceRecorder.getTotalDistance());
    }
}
