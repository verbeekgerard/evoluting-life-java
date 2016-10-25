package eu.luminis;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import eu.luminis.brains.Brain;
import eu.luminis.genetics.Genome;
import eu.luminis.robots.core.Robot;
import eu.luminis.robots.pi.PiMotorsController;
import eu.luminis.robots.pi.PiSensorController;
import eu.luminis.robots.pi.PiServoController;

public class RunRobot {
	
	private static int iteration = 0;
	
	public static void main(String[] args) {

		try {
			String json = new String(readAllBytes(get("testrobot.json")));
			Type listType = new TypeToken<ArrayList<Genome>>(){}.getType();
			List<Genome> genomes = new Gson().fromJson(json, listType);
			
			Genome genome = genomes.get(0);
			PiMotorsController piMotorsController = new PiMotorsController(genome.getMovement().getLinearForce());
			PiServoController piServoController = new PiServoController(genome.getMovement().getAngularForce());
			PiSensorController piSensorController = new PiSensorController();
			
			Robot robot = new Robot(new Brain(genome.getBrain()), piMotorsController, piServoController, piSensorController);
			loop(robot);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loop(Robot robot) {
		System.out.println("Iteration: " + iteration++);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		robot.run();	
		loop(robot);
	}
	
}
