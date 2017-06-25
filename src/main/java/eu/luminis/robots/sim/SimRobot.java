package eu.luminis.robots.sim;

import eu.luminis.Options;
import eu.luminis.brains.IBrain;
import eu.luminis.events.EventType;
import eu.luminis.evolution.CostCalculator;
import eu.luminis.genetics.Genome;
import eu.luminis.geometry.Position;
import eu.luminis.geometry.Velocity;
import eu.luminis.robots.core.IAngleRetriever;
import eu.luminis.robots.core.Robot;

public class SimRobot extends SimObstacle implements Comparable<SimRobot> {
    private static final double initialEnergy = Options.initialEnergy.get();
    private static final CostCalculator costCalculator = CostCalculator.getInstance();

    private final Genome genome;
    private final SimServoAngleRecorder simServoAngleRecorder;
    private final SimMovementRecorder simMovementRecorder;

    private final Robot robot;
    private final SimSensorController sensorController;

    private final double size;

    private double cycleCost = 0;
    private double collisionDamage = 0;

    private boolean isColliding = false;
    private Position targetObstaclePosition;

    public SimRobot(Genome genome, SimWorld world, IBrain brain, SimLife simLife, SimMovementRecorder simMovementRecorder, SimServoAngleRecorder simServoAngleRecorder) {
        super(world, simMovementRecorder, simLife);

        this.genome = genome;
        this.size = Options.sizeOption.get();

        this.simMovementRecorder = simMovementRecorder;
        this.simServoAngleRecorder = simServoAngleRecorder;

        SimMotorsController motorsController = initializeMotorsController(genome);
        SimServoController servoController = initializeServoController(genome);
        sensorController = initializeSensorController(genome);
        robot = new Robot(
                brain,
                motorsController,
                servoController,
                sensorController
        );
    }

    public Double fitness() {
        return (double)getAgeInformation().getAge();
    }

    public Double health() {
        return initialEnergy + getDistanceReward() - cycleCost - collisionDamage - simMovementRecorder.getMovementCost() - simServoAngleRecorder.getHeadTurnCost();
    }

    public boolean isColliding() {
        return isColliding;
    }

    @Override
    public int compareTo(SimRobot other) {
        int compareResult = other.fitness().compareTo(fitness());
        if (compareResult != 0) {
            return compareResult;
        }

        return other.health().compareTo(health());
    }

    @Override
    public double getSize() {
        double health = health();
        double healthN = health > 0 ? 1 - 1 / Math.exp(health / 200) : 0;

        return size * (1 + 0.75 * healthN);
    }

    public Genome getGenome() {
        return genome;
    }

    public void recordCollision() {
        Velocity velocity = simMovementRecorder.getVelocity();
        collisionDamage += costCalculator.collide(velocity.getMagnitude());

        preventOverlap(velocity);

        eventBroadcaster.broadcast(EventType.COLLIDE, collisionDamage);
    }

    public IAngleRetriever getServo() {
        return simServoAngleRecorder;
    }

    public double getTravelledDistance() {
        return simMovementRecorder.getTotalDistance();
    }

    public Position getTargetObstaclePosition() {
        return targetObstaclePosition;
    }

    public void setTargetObstaclePosition(Position position) {
        this.targetObstaclePosition = position;
    }

    public double getViewDistance() {
        return sensorController.getViewDistance();
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
        return health() > 0;
    }

    private SimMotorsController initializeMotorsController(Genome genome) {
        return new SimMotorsController(simMovementRecorder, genome.getMovement().getLinearForce());
    }

    private SimServoController initializeServoController(Genome genome) {
        return new SimServoController(simServoAngleRecorder, genome.getSensor().getFieldOfView(), genome.getMovement().getAngularForce());
    }

    private SimSensorController initializeSensorController(Genome genome) {
        return new SimSensorController(this, this.simMovementRecorder, genome.getSensor().getFieldOfView(), genome.getSensor().getViewDistance(), simServoAngleRecorder);
    }

    private double getDistanceReward() {
        return costCalculator.distanceReward(getTravelledDistance());
    }

    private void preventOverlap(Velocity velocity) {
        Position position = getPosition();
        position.Subtract(velocity); // Move the entity opposite to it's velocity
    }
}
