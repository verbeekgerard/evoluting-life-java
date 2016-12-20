package eu.luminis.robots.pi.util;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

import java.io.IOException;

public class Led {
    private final GpioPinDigitalOutput led;

    public Led(GpioController gpio, Pin pin) {
        led = gpio.provisionDigitalOutputPin(pin, PinState.LOW);
    }

    public void blink(long interval, long times) {
        // interrupt active blinking thread
        led.blink(interval, 0);

        // start new blinking thread
        led.blink(interval, interval * times);
    }

    public void shutdown() throws IOException {
        led.low();
    }
}
