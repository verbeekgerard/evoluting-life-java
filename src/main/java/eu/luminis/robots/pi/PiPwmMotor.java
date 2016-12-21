package eu.luminis.robots.pi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;

public class PiPwmMotor {
    private final GpioPinPwmOutput forward;
    private final GpioPinPwmOutput reverse;

    public PiPwmMotor(GpioController gpio, Pin forwardPin, Pin reversePin) {
        forward = gpio.provisionPwmOutputPin(forwardPin, 0);
        reverse = gpio.provisionSoftPwmOutputPin(reversePin, 0);
    }

    public void stop() {
        forward.setPwm(0); // 0 to 1024
        reverse.setPwm(0); // 0 to 100
    }

    public void forward(double duty) {
        // System.out.println("forward: " + (int) (1024 * duty));
        forward.setPwm((int) (1024 * duty)); // 0 to 1024
        reverse.setPwm(0); // 0 to 100
    }

    public void reverse(double duty) {
        // System.out.println("reverse: " + (int) (100 * duty));
        forward.setPwm(0); // 0 to 1024
        reverse.setPwm((int) (100 * duty)); // 0 to 100
    }

    public void shutdown() {
        forward.setPwm(0);
        reverse.setPwm(0);
    }
}