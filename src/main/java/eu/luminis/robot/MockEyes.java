package eu.luminis.robot;

import java.util.List;

import eu.luminis.entities.Animal;
import eu.luminis.entities.Plant;
import eu.luminis.sensors.Eyes;

public class MockEyes implements Eyes {

	@Override
	public Double sense(List<Plant> plants, List<Animal> animals) {
		// Distance from sensor
		// 0 to 50
		System.out.println("Sense");
		return 1.0;
	}

}
