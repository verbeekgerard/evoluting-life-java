package eu.luminis;

import java.io.IOException;
import java.util.List;

import eu.luminis.brains.BrainBuilder;
import eu.luminis.brains.IBrain;
import eu.luminis.genetics.Genome;
import eu.luminis.robots.core.Robot;
import eu.luminis.robots.pi.PiMotorsController;
import eu.luminis.robots.pi.PiSensorController;
import eu.luminis.robots.pi.PiServoController;
import eu.luminis.util.GenesFile;

public class RunRobot {

    private static int iteration = 0;

    public static void main(String[] args) {

        try {
            GenesFile genesFile = new GenesFile("testrobot.json");
            List<Genome> genomes = genesFile.read();

            Robot robot = createRobot(genomes.get(0));
            loop(robot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loop(Robot robot) {
        System.out.println("Iteration: " + iteration++);

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        robot.run();
        loop(robot);
    }

    private static Robot createRobot(Genome genome) {
        IBrain brain = initializeBrain(genome);
        PiMotorsController piMotorsController = new PiMotorsController(genome.getMovement().getLinearForce());
        PiServoController piServoController = new PiServoController(genome.getMovement().getAngularForce());
        PiSensorController piSensorController = new PiSensorController();

        return new Robot(brain, piMotorsController, piServoController, piSensorController);
    }

    private static IBrain initializeBrain(Genome genome) {
        BrainBuilder builder = new BrainBuilder(genome.getBrain());
        return builder.build();
    }
}