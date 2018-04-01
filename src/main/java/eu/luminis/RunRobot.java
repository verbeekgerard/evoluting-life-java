package eu.luminis;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.pi4j.io.gpio.GpioController;
import eu.luminis.brains.*;
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
            Options.brainIsRecurrent = getRecurrentNeuralNetwork(args);
            System.out.println("Using " + (Options.brainIsRecurrent ?
                    "recurrent neural networks" :
                    "feed forward neural networks"));

            String genesFilePath = getFilePath(args);
            System.out.println("Reading genes file: " + genesFilePath);
            GenesFile genesFile = new GenesFile(genesFilePath);
            List<Genome> genomes = genesFile.read();

            int genomeIndex = getGenomeIndex(args);
            System.out.println("Creating a robot from genome: " + genomeIndex);
            Robot robot = createRobot(genomes.get(genomeIndex));

            int loopDelay = getLoopDelay(args);
            System.out.println("Start robot with loopDelay (ms): " + loopDelay);
            loop(robot, loopDelay);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loop(Robot robot, int loopDelay) {
        looping = true;
        while (looping){
            // System.out.println("---------------------------------");
            robot.run();

            if (loopDelay > 0) {
                sleep(loopDelay);
            }
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
        return GRUNeuralNetworkBuilder.create()
            .withNeuralNetworkGene(genome.getBrain())
            .build();
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

    private static int getLoopDelay(String[] args) {
        int delay = 0;
        if(args.length > 2) {
            delay = Integer.parseInt(args[2]);
        }

        return delay;
    }

    private static boolean getRecurrentNeuralNetwork(String[] args){
        return Arrays.asList(args).indexOf("rnn") > -1;
    }
}
