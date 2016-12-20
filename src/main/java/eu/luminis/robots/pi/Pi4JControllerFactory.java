package eu.luminis.robots.pi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

public class Pi4JControllerFactory {
    private static final GpioController gpio = GpioFactory.getInstance();

    public static GpioController GetController() {
        return gpio;
    }
}
