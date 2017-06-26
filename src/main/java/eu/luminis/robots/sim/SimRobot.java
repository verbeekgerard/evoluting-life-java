package eu.luminis.robots.sim;

import eu.luminis.Options;
import eu.luminis.brains.IBrain;
import eu.luminis.events.EventType;
import eu.luminis.evolution.CostCalculator;
import eu.luminis.genetics.Genome;
import eu.luminis.geometry.Vector;
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

    private double health = initialEnergy;
    private double cycleCost = 0;
    private double collisionDamage = 0;

    private boolean isColliding = false;
    private Vector targetObstaclePosition;

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
        return health;
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
        return size;
    }

    public Genome getGenome() {
        return genome;
    }

    public void recordCollision() {
        Vector velocity = simMovementRecorder.getVelocity();
        collisionDamage += costCalculator.collide(velocity.getLength());

        simMovementRecorder.preventOverlap();

        eventBroadcaster.broadcast(EventType.COLLIDE, collisionDamage);
    }

    public IAngleRetriever getServo() {
        return simServoAngleRecorder;
    }

    public double getTravelledDistance() {
        return simMovementRecorder.getTotalDistance();
    }

    public Vector getTargetObstaclePosition() {
        return targetObstaclePosition;
    }

    public void setTargetObstaclePosition(Vector position) {
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

        health = initialEnergy + getDistanceReward() - cycleCost - collisionDamage - simMovementRecorder.getMovementCost() - simServoAngleRecorder.getHeadTurnCost();
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
}
