package eu.luminis.robots.pi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.RaspiPin;
import eu.luminis.robots.core.ISensorController;
import eu.luminis.robots.pi.util.Led;

import java.util.Collections;
import java.util.function.Consumer;

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
        piSensor = new PiSensor(gpio, RaspiPin.GPIO_14, RaspiPin.GPIO_10);
    }

    @Override
	public double sense() {
	    Double d = piSensor.sense(PiSensor.echoWaitMs * 2);
        distance = d == null ? viewDistance : d > viewDistance ? viewDistance : d;

        // System.out.println("Sense: " + distance);
        led.dim((viewDistance - distance) / viewDistance);

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
}
