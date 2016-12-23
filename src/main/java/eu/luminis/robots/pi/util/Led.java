package eu.luminis.robots.pi.util;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;

public class Led {
    private final GpioPinPwmOutput led;

    public Led(GpioController gpio, Pin pin) {
        led = gpio.provisionSoftPwmOutputPin(pin, 0);
    }

    public void dim(double duty) {
        led.setPwm((int)(duty * 100)); // 0 to 100
    }

    public void shutdown() {
        led.setPwm(0);
    }
}
