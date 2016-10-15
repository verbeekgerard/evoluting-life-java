package eu.luminis.robots;

import eu.luminis.brains.Brain;
import eu.luminis.entities.Position;
import eu.luminis.entities.World;
import eu.luminis.genetics.BrainGene;
import eu.luminis.genetics.Genome;

public class SimRobot extends Obstacle {
    private final Genome genome;
    private final Robot robot;

    public SimRobot(Genome genome, Position position, World world) {
        super(world, position);

        this.genome = genome;

        BrainGene brainGene = genome.getBrain();
        this.robot = new Robot(
                new Brain(brainGene),
                new SimMotorsController(this),
                new SimServo(this),
                new SimSensor(this)
        );
    }

    public void run() {
        this.robot.run();
    }
}
