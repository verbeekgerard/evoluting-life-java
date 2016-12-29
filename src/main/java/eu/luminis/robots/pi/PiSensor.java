package eu.luminis.robots.pi;

import com.pi4j.io.gpio.*;
import eu.luminis.robots.pi.util.Sensor;

import java.util.concurrent.*;

public class PiSensor extends Sensor<Double> {
    private static final int SPEEDOFSOUND_CM_S = 34029;

    private final GpioPinDigitalOutput trigger;
    private final GpioPinDigitalInput echo;
    private final long echoWaitNs;

    public PiSensor(GpioController gpio, Pin triggerPin, Pin echoPin, double maxDistanceCm) {
        trigger = gpio.provisionDigitalOutputPin(triggerPin, PinState.LOW);
        echo = gpio.provisionDigitalInputPin(echoPin, PinPullResistance.PULL_UP);

        // v = cm/s => s = cm/v => cm/34029 => nanoseconds = 1.000.000.000 * cm/34029
        echoWaitNs = 2 * ((long)(1000000000L * maxDistanceCm / SPEEDOFSOUND_CM_S) + 1);
    }

    @Override
    public Double sense() {
        trigger.pulse(1, true);

        long startTime = System.nanoTime(); //ns
        long start = startTime;
        //echo will go 0 to 1 and need to save time for that. 20 milliseconds difference
        while (echo.isLow() && start < startTime + echoWaitNs) {
            start = System.nanoTime();
        }

        if (start == startTime || start >= startTime + echoWaitNs) {
            return null;
        }

        long stop = start;
        while (echo.isHigh() && stop < start + echoWaitNs) {
            stop = System.nanoTime();
        }

        if (stop == start || stop >= start + echoWaitNs) {
            return null;
        }

        double deltaSeconds = (stop - start) * SPEEDOFSOUND_CM_S / 1000000000L;
        return deltaSeconds / 2.0;
    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

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

    long getEchoWaitNs() {
        return echoWaitNs;
    }
}