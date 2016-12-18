package eu.luminis.robots.pi;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import eu.luminis.robots.pi.util.Sensor;
import eu.luminis.robots.pi.util.SleepUtil;
import jdk.dio.DeviceConfig;
import jdk.dio.DeviceManager;
import jdk.dio.gpio.GPIOPin;
import jdk.dio.gpio.GPIOPinConfig;

public class PiSensor extends Sensor<Double> {

    private static final int PULSE_NS = 10000;
    private static final int SPEEDOFSOUND_CM_S = 34029;
    private static final long ECHO_WAIT_NS = 10000000L * 2; // 20 ms

    private GPIOPin triggerPin;
    private GPIOPin echoPin;

    public PiSensor(int triggerPinNumber, int echoPinNumber) throws IOException {
        triggerPin = DeviceManager.open(new GPIOPinConfig(DeviceConfig.DEFAULT, triggerPinNumber,
                GPIOPinConfig.DIR_OUTPUT_ONLY, GPIOPinConfig.MODE_OUTPUT_PUSH_PULL, GPIOPinConfig.TRIGGER_NONE, false));
        echoPin = DeviceManager.open(new GPIOPinConfig(DeviceConfig.DEFAULT, echoPinNumber,
                GPIOPinConfig.DIR_INPUT_ONLY, GPIOPinConfig.MODE_INPUT_PULL_UP, GPIOPinConfig.TRIGGER_NONE, false));
    }

    @Override
    public Double sense() throws IOException {
        triggerSensor();

        long startTime = System.nanoTime(); //ns
        long start = startTime;
        //echo will go 0 to 1 and need to save time for that. 20 milliseconds difference
        while (!echoPin.getValue() && start < startTime + ECHO_WAIT_NS) {
            start = System.nanoTime();
        }

        if (start == startTime || start >= startTime + ECHO_WAIT_NS) {
            return null;
        }

        long stop = start;
        while (echoPin.getValue() && stop < start + ECHO_WAIT_NS) {
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
    public void shutdown() throws IOException {
        echoPin.close();
        triggerPin.close();
        executorService.shutdownNow();
        super.shutdown();
    }

    private void triggerSensor() throws IOException {
        triggerPin.setValue(true);
        SleepUtil.sleep(0, PULSE_NS);
        triggerPin.setValue(false);
    }
}