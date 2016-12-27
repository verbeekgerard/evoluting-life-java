package eu.luminis.robots.pi;

import com.pi4j.io.gpio.*;
import eu.luminis.robots.pi.util.Sensor;

import java.util.concurrent.*;

public class PiSensor extends Sensor<Double> {
    private static final int SPEEDOFSOUND_CM_S = 34029;
    private static final long ECHO_WAIT_NS = 1000000L * 12; // 12 ms => 0.012 sec * 34029 m/sec => 2.04m distance

    private final GpioPinDigitalOutput trigger;
    private final GpioPinDigitalInput echo;

    public PiSensor(GpioController gpio, Pin triggerPin, Pin echoPin) {
        trigger = gpio.provisionDigitalOutputPin(triggerPin, PinState.LOW);
        echo = gpio.provisionDigitalInputPin(echoPin, PinPullResistance.PULL_UP);
    }

    @Override
    public Double sense() {
        trigger.pulse(1, true);

        long startTime = System.nanoTime(); //ns
        long start = startTime;
        //echo will go 0 to 1 and need to save time for that. 20 milliseconds difference
        while (echo.isLow() && start < startTime + ECHO_WAIT_NS) {
            start = System.nanoTime();
        }

        if (start == startTime || start >= startTime + ECHO_WAIT_NS) {
            return null;
        }

        long stop = start;
        while (echo.isHigh() && stop < start + ECHO_WAIT_NS) {
            stop = System.nanoTime();
        }

        if (stop == start || stop >= start + ECHO_WAIT_NS) {
            return null;
        }

        double deltaSeconds = (stop - start) * SPEEDOFSOUND_CM_S / 1000000000L;
        return deltaSeconds / 2.0;
    }

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public Double sense(long msTimeout) {
        Callable<Double> callableSense = this::sense;

        Future<Double> future = executorService.submit(callableSense);

        try {
            return future.get(msTimeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return null;
        }
    }

    @Override
    public void shutdown() {
        trigger.low();
        executorService.shutdownNow();
        super.shutdown();
    }
}