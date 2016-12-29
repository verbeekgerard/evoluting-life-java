package eu.luminis;

import java.io.IOException;
import java.util.List;

import com.pi4j.io.gpio.GpioController;
import eu.luminis.brains.BrainBuilder;
import eu.luminis.brains.IBrain;
import eu.luminis.genetics.Genome;
import eu.luminis.robots.core.IMotorsController;
import eu.luminis.robots.core.ISensorController;
import eu.luminis.robots.core.IServoController;
import eu.luminis.robots.core.Robot;
import eu.luminis.robots.pi.*;
import eu.luminis.util.GenesFile;

import static eu.luminis.robots.pi.util.SleepUtil.sleep;

public class RunRobot {
    private final static GpioController gpio = Pi4JControllerFactory.GetController();
    private static boolean looping = false;

    public static void main(String[] args) {
        try {
            String genesFilePath = getFilePath(args);
            System.out.println("Reading genes file: " + genesFilePath);
            GenesFile genesFile = new GenesFile(genesFilePath);
            List<Genome> genomes = genesFile.read();

            int genomeIndex = getGenomeIndex(args);
            System.out.println("Creating a robot from genome: " + genomeIndex);
            Robot robot = createRobot(genomes.get(genomeIndex));

            System.out.println("Start robot");
            loop(robot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loop(Robot robot) {
        looping = true;
        while (looping){
            // System.out.println("---------------------------------");
            robot.run();
        }
    }

    private static Robot createRobot(Genome genome)  {
        IBrain brain = initializeBrain(genome);
        IPiController piMotorsController = initializeMotorsController(genome);
        IPiController piServoController = initializeServoController(genome);
        IPiController piSensorController = initializeSensorController(genome);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown hook called");

            looping = false;
            sleep(200);

            piSensorController.shutdown();
            piServoController.shutdown();
            piMotorsController.shutdown();
            gpio.shutdown();

            System.out.println("Shutdown completed");
        }));

        return new Robot(brain,
                (IMotorsController) piMotorsController,
                (IServoController) piServoController,
                (ISensorController) piSensorController);
    }

    private static IBrain initializeBrain(Genome genome) {
        BrainBuilder builder = new BrainBuilder(genome.getBrain());
        return builder.build();
    }

    private static IPiController initializeMotorsController(Genome genome) {
        double linearForce = genome.getMovement().getLinearForce();
        System.out.println("Initializing motors with linearForce: " + linearForce);

        return new PiPwmMotorsController(linearForce);
    }

    private static IPiController initializeServoController(Genome genome) {
        double fieldOfView = genome.getSensor().getFieldOfView();
        double angularForce = genome.getMovement().getAngularForce();

        System.out.println("Initializing servo with fieldOfView: " + fieldOfView );
        System.out.println("Initializing servo with angularForce: " + angularForce);

        return new PiServoController(fieldOfView, angularForce);
    }

    private static IPiController initializeSensorController(Genome genome) {
        double viewDistance = genome.getSensor().getViewDistance();
        System.out.println("Initializing sensor with viewDistance: " + viewDistance);

        return new PiSensorController(viewDistance);
    }

    private static String getFilePath(String[] args) {
        String filePath = "testrobot.json";
        if(args.length > 0) {
            filePath = args[0];
        }

        return filePath;
    }

    private static int getGenomeIndex(String[] args) {
        int index = 0;
        if(args.length > 1) {
            index = Integer.parseInt(args[1]);
        }

        return index;
    }
}
