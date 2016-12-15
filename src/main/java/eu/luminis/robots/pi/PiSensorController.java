package eu.luminis.robots.pi;

import java.io.IOException;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Predicate;

import eu.luminis.robots.core.ISensorController;

public class PiSensorController implements ISensorController {
	private static final double viewDistance = 80; // TODO: Figure out what the true viewDistance is

	private PiSensor piSensor;
	private Double distance = viewDistance;

	public PiSensorController() {
		try {
			piSensor = new PiSensor(19, 16);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// average of latest 3, remove min and max,
		piSensor.enableNoiseReduction(5, values -> {
			Collections.sort(values);

			return values
				.subList(1, values.size() - 1)
				.stream()
				.mapToDouble(x -> x)
				.average()
				.getAsDouble();
		});

		Consumer<Double> callback = d -> {
            distance = d == null ? viewDistance : d;
			System.out.println("Sense: " + distance);
		};

		Predicate<Double> filter = d -> d == null || d < viewDistance;

		piSensor.addSensorCallback(filter, callback);

		piSensor.start(50);
	}

	@Override
	public double sense() {
		return distance;
	}

	@Override
	public double getViewDistance() {
		return viewDistance;
	}
}
