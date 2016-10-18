package eu.luminis.robots;

import eu.luminis.brains.Brain;
import eu.luminis.genetics.BrainGene;
import eu.luminis.genetics.Genome;

public class PiRobot {

	private final Robot robot;
    private final PiMotorsController motorsController;
    private final PiSensorController sensorController;
    private Genome genome;
	
	public PiRobot(Genome genome) {
        this.genome = genome;
        BrainGene brainGene = genome.getBrain();
        motorsController = new PiMotorsController();
        PiServoController servoController = new PiServoController();
        sensorController = new PiSensorController(servoController);
        robot = new Robot(
                new Brain(brainGene),
                motorsController,
                servoController,
                sensorController
        );
    }
	
	public void run() {
        robot.run();
    }
}
