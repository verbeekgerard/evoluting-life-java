package eu.luminis.robots;

import eu.luminis.brains.Brain;
import eu.luminis.entities.*;
import eu.luminis.general.CostCalculator;
import eu.luminis.general.EventType;
import eu.luminis.general.Options;
import eu.luminis.genetics.BrainGene;
import eu.luminis.genetics.Genome;

public class SimRobot extends Obstacle implements Comparable<SimRobot> {
    private CostCalculator costCalculator = CostCalculator.getInstance();

    private final Robot robot;
    private final SimMotorsController motorsController;
    private final SimSensorController sensorController;
    private Genome genome;
    private TravelledDistanceRecorder distanceRecorder;
    private Position position;
    private double initialEnergy;
    private double movementCost = 0;
    private double headTurnCost = 0;
    private double collisionDamage = 0;

    private double size;
    private boolean isColliding = false;

    public SimRobot(Genome genome, Position position, SimWorld world) {
        super(world, position);
        this.genome = genome;
        this.position = position;
        this.distanceRecorder = new TravelledDistanceRecorder(position);
        BrainGene brainGene = genome.getBrain();
        motorsController = new SimMotorsController(this, genome.getMovement().getLinearForce());
        SimServoController servoController = new SimServoController(this);
        sensorController = new SimSensorController(this, genome.getSensor().getViewDistance(), servoController);
        robot = new Robot(
                new Brain(brainGene),
                motorsController,
                servoController,
                sensorController
        );

        this.initialEnergy = Options.initialEnergyOption.get();
        
        this.size = Options.sizeOption.get();
    }
    
    public SimSensorController getSensorController(){
    	return this.sensorController;
    }
    
    public Position getPosition(){
    	return position;
    }
    
    public Genome getGenome() {
        return genome;
    }

    public void run() {
        sensorController.prepareForNearbyObstacles();
        robot.run();
        isColliding = sensorController.isColliding();
    }
    
    @Override
    public int compareTo(SimRobot otherRobot) {
        return otherRobot.fitness().compareTo(this.fitness());
    }

    public Double fitness() {
        return this.initialEnergy + getDistanceReward() - collisionDamage - movementCost - headTurnCost;
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

    public void recordAngleChange(double acceleration) {
        headTurnCost += costCalculator.turnHead(acceleration);
    }
    
    public boolean lives() {
        Position p = this.getPosition();

        if (p.x > world.getWidth() || p.x < 0 || p.y > world.getHeight() || p.y < 0) {
            eventBroadcaster.broadcast(EventType.WANDERED, 1);
            return false;
        }

        // Kill eu.luminis.entities if it's exceeded starvation threshold
        if (this.fitness() <= 0) {
            eventBroadcaster.broadcast(EventType.STARVED, 1);
            return false;
        }

//        // Randomly kill eu.luminis.entities who've entered old age
//        if (this.age > this.getOldAge()) {
//
//            // Vulnerable eu.luminis.entities have 1/100 chance of death
//            if (Math.random() * 100 <= 1) {
//                eventBroadcaster.broadcast(EventType.DIED_OF_AGE, 1);
//                return false;
//            }
//        }

        return true;
    }
    
}
