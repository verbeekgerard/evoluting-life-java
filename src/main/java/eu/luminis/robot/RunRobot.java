package eu.luminis.robot;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import eu.luminis.entities.Position;
import eu.luminis.genetics.Genome;
import eu.luminis.robots.PiRobot;

public class RunRobot {
	
	private static int iteration = 0;
	
	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook( new Thread(() -> {
			System.out.println("Stop");
		}));
		
		try {
			String json = new String(readAllBytes(get("testrobot.json")));
			Type listType = new TypeToken<ArrayList<Genome>>(){}.getType();
			List<Genome> genomes = new Gson().fromJson(json, listType);
			
			PiRobot robot = new PiRobot(genomes.get(0));
			loop(robot);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loop(PiRobot robot) {
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
