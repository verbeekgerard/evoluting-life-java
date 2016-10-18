package eu.luminis.robots;

import java.io.IOException;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Predicate;

import eu.luminis.robot.DistanceSensor;

public class PiSensor {
	
	private DistanceSensor distanceSensor;
	private Double distance;
	
	public PiSensor() {
		try {
			distanceSensor = new DistanceSensor(19, 16);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// average of latest 3, remove min and max,
		distanceSensor.enableNoiseReduction(5, values -> {
			Collections.sort(values);
			return values.subList(1, values.size() - 1)
					.stream()
					.mapToDouble(x -> x)
					.average()
					.getAsDouble();
		});
		
		Consumer<Double> callback = d -> {
			distance = d;
			System.out.println(d);
		};
		
		Predicate<Double> filter = d -> {
			return d != null && d < 50;
		};
		
		distanceSensor.addSensorCallback(filter, callback);
		
		distanceSensor.start(100);
	}
	
    public Double sense() {
    	return distance;
    }
    
}
