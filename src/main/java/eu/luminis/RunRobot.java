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
import eu.luminis.robots.pi.*;
import eu.luminis.util.GenesFile;

public class RunRobot {

    // private static int iteration = 0;

    public static void main(String[] args) {

        try {
            String genesFilePath = getFilePath(args);
            GenesFile genesFile = new GenesFile(genesFilePath);
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
                // System.out.println("Iteration: " + iteration++);
                robot.run();
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static Robot createRobot(Genome genome) throws IOException {
        IBrain brain = initializeBrain(genome);
        IPiController piMotorsController = initializeMotorsController(genome);
        IPiController piServoController = initializeServoController(genome);
        IPiController piSensorController = initializeSensorController(genome);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown hook called");
            try {
                piServoController.shutdown();
                piSensorController.shutdown();
                piMotorsController.shutdown();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }));

        return new Robot(brain,
                (IMotorsController)piMotorsController,
                (IServoController) piServoController,
                (ISensorController)piSensorController);
    }

    private static IBrain initializeBrain(Genome genome) {
        BrainBuilder builder = new BrainBuilder(genome.getBrain());
        return builder.build();
    }

    private static IPiController initializeMotorsController(Genome genome) throws IOException {
        double linearForce = genome.getMovement().getLinearForce();
        System.out.println("Initializing motors with linearForce: " + linearForce);

        return new PiPwmMotorsController(linearForce);
    }

    private static IPiController initializeServoController(Genome genome) {
        double fieldOfView = genome.getSensor().getFieldOfView();
        double angularForce = genome.getMovement().getAngularForce();

        System.out.println("Initializing servo with fieldOfView: " + fieldOfView);
        System.out.println("Initializing servo with angularForce: " + angularForce);

        return new PiServoController(fieldOfView, angularForce);
    }

    private static IPiController initializeSensorController(Genome genome) throws IOException {
        double viewDistance = genome.getSensor().getViewDistance();
        System.out.println("Initializing sensor with viewDistance: " + viewDistance);

        return new PiSensorController(viewDistance);
    }

    private static String getFilePath(String[] args){
        String filePath = "testrobot.json";
        if(args.length > 0) {
            filePath = args[0];
        }

        return filePath;
    }
}