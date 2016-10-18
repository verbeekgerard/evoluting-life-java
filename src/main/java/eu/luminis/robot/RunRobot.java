package eu.luminis.robot;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import eu.luminis.entities.Animal;
import eu.luminis.entities.Plant;
import eu.luminis.entities.Position;
import eu.luminis.entities.World;
import eu.luminis.genetics.Genome;
import eu.luminis.sensors.Eyes;

public class RunRobot {
	
	private static World world = new World();
	private static int iteration = 0;
	
	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook( new Thread(() -> {
			System.out.println("Stop");
		}));
		
		try {
			String json = new String(readAllBytes(get("testrobot.json")));
			Type listType = new TypeToken<ArrayList<Genome>>(){}.getType();
			List<Genome> genomes = new Gson().fromJson(json, listType);
			
//			Robot r = new MockRobot();
//			Eyes e = new MockEyes();
			Robot r = new PiRobot();
			Eyes e = new RobotEyes();
			
			RobotAnimal robot = new RobotAnimal(genomes.get(0), new Position(0, 0, 0), world, r, e);
			loop(robot);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loop(Animal robot) {
		System.out.println("Iteration: " + iteration++);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		robot.run(new ArrayList<Plant>(), new ArrayList<Animal>());	
		loop(robot);
	}
	
}
