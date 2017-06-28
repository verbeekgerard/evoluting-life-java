package eu.luminis.robots.sim;

import eu.luminis.Options;
import eu.luminis.evolution.CostCalculator;
import eu.luminis.genetics.Genome;
import eu.luminis.geometry.Vector;
import eu.luminis.robots.core.IAngleRetriever;
import eu.luminis.robots.core.Robot;

public class SimRobot extends SimObstacle implements Comparable<SimRobot> {
    private static final double initialEnergy = Options.initialEnergy.get();
    private static final CostCalculator costCalculator = CostCalculator.getInstance();

    private final Genome genome;
    private final SimCollisionRecorder collisionRecorder;
    private final SimServoAngleRecorder simServoAngleRecorder;
    private final SimMovementRecorder simMovementRecorder;

    private final Robot robot;
    private final SimSensorController sensorController;

    private double health = initialEnergy;
    private double cycleCost = 0;

    private boolean isColliding = false;
    private double travelledDistance = 0;

    public SimRobot(Genome genome, SimWorld world, Robot robot, SimLife simLife,
                    SimMovementRecorder simMovementRecorder,
                    SimServoAngleRecorder simServoAngleRecorder,
                    SimCollisionRecorder collisionRecorder,
                    SimSensorController sensorController) {
        super(world, simMovementRecorder, simLife);

        this.genome = genome;
        this.robot = robot;
        this.simMovementRecorder = simMovementRecorder;
        this.simServoAngleRecorder = simServoAngleRecorder;
        this.collisionRecorder = collisionRecorder;

        this.sensorController = sensorController;
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
        return collisionRecorder.getSize();
    }

    public Genome getGenome() {
        return genome;
    }

    public IAngleRetriever getServo() {
        return simServoAngleRecorder;
    }

    public double getTravelledDistance() {
        return travelledDistance;
    }

    public Vector getTargetObstaclePosition() {
        return collisionRecorder.getTargetObstaclePosition();
    }

    public double getViewDistance() {
        return collisionRecorder.getViewDistance();
    }

    @Override
    protected void run() {
        sensorController.prepareForNearbyObstacles();
        robot.run();
        isColliding = sensorController.isColliding();
        travelledDistance = simMovementRecorder.getTotalDistance();

        cycleCost += costCalculator.cycle();

        health = initialEnergy + getDistanceReward() - cycleCost -
                collisionRecorder.getCollisionDamage() -
                simMovementRecorder.getMovementCost() -
                simServoAngleRecorder.getHeadTurnCost();
    }

    @Override
    protected boolean isAlive() {
        return health() > 0;
    }

    private double getDistanceReward() {
        return costCalculator.distanceReward(travelledDistance);
    }
}
