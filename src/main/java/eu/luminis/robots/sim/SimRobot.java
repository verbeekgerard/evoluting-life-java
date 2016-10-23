package eu.luminis.robots.sim;

import eu.luminis.brains.Brain;
import eu.luminis.evolution.CostCalculator;
import eu.luminis.events.EventType;
import eu.luminis.general.Options;
import eu.luminis.genetics.BrainGene;
import eu.luminis.genetics.Genome;
import eu.luminis.geometry.Position;
import eu.luminis.robots.core.IAngleRetriever;
import eu.luminis.robots.core.Robot;

public class SimRobot extends SimObstacle implements Comparable<SimRobot> {
    private static final double initialEnergy = Options.initialEnergy.get();
    private static final CostCalculator costCalculator = CostCalculator.getInstance();

    private final Genome genome;
    private final Robot robot;
    private final SimMotorsController motorsController;
    private final SimServoController servoController;
    private final SimSensorController sensorController;

    private final TravelledDistanceRecorder distanceRecorder;
    private final double size;

    private double cycleCost = 0;
    private double movementCost = 0;
    private double headTurnCost = 0;
    private double collisionDamage = 0;

    private boolean isColliding = false;

    public SimRobot(Genome genome, Position position, SimWorld world) {
        super(world, position, genome.getLife());
        this.genome = genome;
        this.size = Options.sizeOption.get();
        this.distanceRecorder = new TravelledDistanceRecorder(position);

        Brain brain = initializeBrain(genome);
        motorsController = initializeMotorsController(genome);
        servoController = initializeServoController(genome);
        sensorController = initializeSensorController(genome);
        robot = new Robot(
                brain,
                motorsController,
                servoController,
                sensorController
        );
    }

    public Double fitness() {
        return initialEnergy + getDistanceReward() - cycleCost - collisionDamage - movementCost - headTurnCost;
    }

    public boolean isColliding() {
        return isColliding;
    }

    @Override
    public int compareTo(SimRobot other) {
        return other.fitness().compareTo(fitness());
    }

    @Override
    public double getSize() {
        double fitness = fitness();
        double fitnessN = fitness > 0 ? 1 - 1 / Math.exp(fitness / 200) : 0;

        return size * (1 + 0.75 * fitnessN);
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
        collisionDamage += costCalculator.collide(velocity);

        preventOverlap(velocity);

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

        cycleCost += costCalculator.cycle();
    }

    @Override
    protected boolean isAlive() {
        return fitness() > 0;
    }

    private Brain initializeBrain(Genome genome) {
        return new Brain(genome.getBrain());
    }

    private SimMotorsController initializeMotorsController(Genome genome) {
        return new SimMotorsController(this, genome.getMovement().getLinearForce());
    }

    private SimServoController initializeServoController(Genome genome) {
        return new SimServoController(this, genome.getSensor().getFieldOfView(), genome.getMovement().getAngularForce());
    }

    private SimSensorController initializeSensorController(Genome genome) {
        return new SimSensorController(this, genome.getSensor().getFieldOfView(), genome.getSensor().getViewDistance(), servoController);
    }

    private double getDistanceReward() {
        return costCalculator.distanceReward(distanceRecorder.getTotalDistance());
    }

    private void preventOverlap(double velocity) {
        Position position = getPosition();

        double dx = Math.cos(position.a) * velocity;
        double dy = Math.sin(position.a) * velocity;

        // Move the entity opposite to it's velocity
        position.x -= dx;
        position.y -= dy;
    }
}
