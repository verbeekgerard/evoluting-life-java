package eu.luminis.robots.pi;

import java.io.IOException;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Predicate;

import eu.luminis.robots.core.ISensorController;

public class PiSensorController implements ISensorController {
	private final double viewDistance;
	private double distance;

	public PiSensorController(double viewDistance) {
		this.viewDistance = viewDistance;
		this.distance = viewDistance;

        Consumer<Double> callback = d -> {
            distance = d == null ? viewDistance : d;
            System.out.println("Sense: " + distance);
        };

        Predicate<Double> filter = d -> d == null || d < viewDistance;

        PiSensor piSensor;

		try {
			piSensor = new PiSensor(19, 16);
            configureNoiseReduction(piSensor);
            piSensor.addSensorCallback(filter, callback);

            piSensor.start(100);
        } catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
	public double sense() {
		return distance;
	}

	@Override
	public double getViewDistance() {
		return viewDistance;
	}

    private static void configureNoiseReduction(PiSensor piSensor) {
        piSensor.enableNoiseReduction(5, values -> {
            Collections.sort(values);

            return values
                    .subList(1, values.size() - 1)
                    .stream()
                    .mapToDouble(x -> x)
                    .average()
                    .getAsDouble();
        });
    }
}
