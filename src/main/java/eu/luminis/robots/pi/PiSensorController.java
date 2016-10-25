package eu.luminis.robots.pi;

import java.io.IOException;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Predicate;

import eu.luminis.robots.core.ISensorController;

public class PiSensorController implements ISensorController {
	
	private PiSensor piSensor;
	private final double viewDistance;
	private Double distance;
	
	public PiSensorController() {
		viewDistance = 80; // TODO
		
		try {
			piSensor = new PiSensor(19, 16);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// average of latest 3, remove min and max,
		piSensor.enableNoiseReduction(5, values -> {
			Collections.sort(values);
			return values.subList(1, values.size() - 1)
					.stream()
					.mapToDouble(x -> x)
					.average()
					.getAsDouble();
		});
		
		Consumer<Double> callback = d -> {
			distance = d;
			System.out.println("Sense: "+ d);
		};
		
		Predicate<Double> filter = d -> {
			return d != null && d < 50;
		};
		
		piSensor.addSensorCallback(filter, callback);
		
		piSensor.start(100);
	}
	
    @Override
    public double sense() {
    	if (distance != null) {
    		return distance;
    	}
    	return viewDistance;
    }

    @Override
    public double getViewDistance() {
        return viewDistance;
    }
}
