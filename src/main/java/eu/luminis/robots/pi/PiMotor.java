package eu.luminis.robots.pi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

public class PiMotor {
    private final GpioPinDigitalOutput forward;
    private final GpioPinDigitalOutput reverse;

    public PiMotor(GpioController gpio, Pin forwardPin, Pin reversePin) {
        forward = gpio.provisionDigitalOutputPin(forwardPin, PinState.LOW);
        reverse = gpio.provisionDigitalOutputPin(reversePin, PinState.LOW);
    }

    public void stop() {
        forward.low();
        reverse.low();
    }

    public void forward() {
        forward.high();
        reverse.low();
    }

    public void reverse() {
        forward.low();
        reverse.high();
    }

    public void shutdown() {
        forward.low();
        reverse.low();
    }
}