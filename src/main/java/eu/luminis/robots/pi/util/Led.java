package eu.luminis.robots.pi.util;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.LongStream;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

import static eu.luminis.robots.pi.util.SleepUtil.sleep;

public class Led {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final GpioPinDigitalOutput led;
    private Future<?> thread;

    public Led(GpioController gpio, Pin pin) {
        led = gpio.provisionDigitalOutputPin(pin, PinState.LOW);
    }

    public void blink(long interval, long times) {
        // interrupt active blinking thread
        if (thread != null && !thread.isDone()) {
            thread.cancel(true);
        }

        // start new blinking thread
        thread = executorService.submit(() -> {
            led.low();
            LongStream.range(0, times*2).forEach((n) -> {
                led.toggle();
                sleep(interval);
            });
            led.low();
        });
    }

    public void shutdown() throws IOException {
        led.low();
        executorService.shutdownNow();
    }
}
