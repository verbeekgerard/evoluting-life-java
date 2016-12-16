package eu.luminis;

import java.io.IOException;
import java.util.List;

import eu.luminis.brains.BrainBuilder;
import eu.luminis.brains.IBrain;
import eu.luminis.genetics.Genome;
import eu.luminis.robots.core.IMotorsController;
import eu.luminis.robots.core.ISensorController;
import eu.luminis.robots.core.IServoController;
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
        while (true){
            try {
                System.out.println("Iteration: " + iteration++);
                robot.run();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static Robot createRobot(Genome genome) {
        IBrain brain = initializeBrain(genome);
        IMotorsController piMotorsController = initializeMotorsController(genome);
        IServoController piServoController = initializeServoController(genome);
        ISensorController piSensorController = initializeSensorController(genome);

        return new Robot(brain, piMotorsController, piServoController, piSensorController);
    }

    private static IBrain initializeBrain(Genome genome) {
        BrainBuilder builder = new BrainBuilder(genome.getBrain());
        return builder.build();
    }

    private static IMotorsController initializeMotorsController(Genome genome) {
        return new PiMotorsController(genome.getMovement().getLinearForce());
    }

    private static IServoController initializeServoController(Genome genome) {
        return new PiServoController(genome.getSensor().getFieldOfView(), genome.getMovement().getAngularForce());
    }

    private static ISensorController initializeSensorController(Genome genome) {
        return new PiSensorController(genome.getSensor().getViewDistance());
    }
}