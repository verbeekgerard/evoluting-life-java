package eu.luminis.robots.pi;

import java.io.IOException;
import java.util.Collections;
import java.util.function.Consumer;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.RaspiPin;
import eu.luminis.robots.core.ISensorController;
import eu.luminis.robots.pi.util.Led;

public class PiSensorController implements ISensorController, IPiController {
    private final static GpioController gpio = Pi4JControllerFactory.GetController();

    private final PiSensor piSensor;
    private final Led led;
	private final double viewDistance;
	private double distance;

	public PiSensorController(double viewDistance) {
		this.viewDistance = viewDistance;
		this.distance = viewDistance;

        led = new Led(gpio, RaspiPin.GPIO_29);
        Consumer<Double> callback = d -> {
            distance = d == null ? viewDistance : d > viewDistance ? viewDistance : d;

            if (distance < viewDistance) {
                led.blink(10, 1);
            }

            // System.out.println("Sense: " + distance);
        };

        piSensor = new PiSensor(gpio, RaspiPin.GPIO_14, RaspiPin.GPIO_10);
        configureNoiseReduction(piSensor);
        piSensor.addSensorCallback(callback);
        piSensor.start();
    }

    @Override
	public double sense() {
		return distance;
	}

	@Override
	public double getViewDistance() {
		return viewDistance;
	}

    @Override
    public void shutdown() {
        piSensor.shutdown();
        led.shutdown();
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
