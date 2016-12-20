package eu.luminis.robots.pi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PiMotor {
    private final GpioPinDigitalOutput forward;
    private final GpioPinDigitalOutput reverse;

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    public PiMotor(GpioController gpio, Pin forwardPin, Pin reversePin) {
        forward = gpio.provisionDigitalOutputPin(forwardPin, PinState.LOW);
        reverse = gpio.provisionDigitalOutputPin(reversePin, PinState.LOW);
    }

    public void stop() {
        executorService.execute(() -> {
            forward.low();
            reverse.low();
        });
    }

    public void forward() {
        executorService.execute(() -> {
            forward.high();
            reverse.low();
        });
    }

    public void reverse() {
        executorService.execute(() -> {
            forward.low();
            reverse.high();
        });
    }

    public void shutdown() {
        forward.low();
        reverse.low();
        executorService.shutdownNow();
    }
}