package eu.luminis.robots.pi.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A base class for things that sense.
 * 
 * A single thread is created to interact with the sensor at a scheduled
 * interval in milliseconds.
 *
 * @param <T>
 *            the type of what the sensor senses.
 */
public abstract class Sensor<T> {

    /**
     * An inner class to store the callback and its filter.
     */
    private class Callback {

        public Callback(Consumer<T> callback, Predicate<T> filter) {
            this.callbackFunction = callback;
            this.filter = filter;
        }

        private Consumer<T> callbackFunction;
        private Predicate<T> filter;
    }

    private List<Callback> callbacks = new ArrayList<>();

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setPriority(Thread.MAX_PRIORITY);
            return t;
        }
    });

    private T sensorData;
    private long senseIntervalInMilliseconds;
    private ScheduledFuture<?> scheduler;

    /**
     * An inner class that allows scheduled sensing and calls back in a separate
     * thread.
     */
    private class SenseTask implements Runnable {

        @Override
        public void run() {
            try {
                sensorData = sense();

                if (sensorData != null) {
                    reduceNoise();
                }

                // present the data to all callbacks
                callbacks.forEach(c -> {
                    if (c.filter.test(sensorData)) {
                        c.callbackFunction.accept(sensorData);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void reduceNoise() {
            if (buffer != null) {
                buffer.add(sensorData);
                if (buffer.filled()) {
                    sensorData = denoiseFunction.apply(buffer.elements());
                } else {
                    sensorData = null;
                }
            }
        }
    }

    /**
     * Start sensing at the given interval.
     * 
     * @param intervalInMilliseconds
     *            the interval in milliseconds
     */
    public void start(long intervalInMilliseconds) {
        this.senseIntervalInMilliseconds = intervalInMilliseconds;

        scheduler = executorService.scheduleAtFixedRate(new SenseTask(), 0, intervalInMilliseconds,
                TimeUnit.MILLISECONDS);
    }

    /**
     * Start sensing continuously.
     */
    public void start() {
        scheduler = executorService.scheduleWithFixedDelay(new SenseTask(), 0, 50, TimeUnit.MILLISECONDS);
    }

    public void pauze() {
        if (scheduler != null && !scheduler.isDone()) {
            scheduler.cancel(true);
        }
    }

    public void resume() {
        if (senseIntervalInMilliseconds > 0) {
            scheduler = executorService.scheduleAtFixedRate(new SenseTask(), 0, senseIntervalInMilliseconds,
                    TimeUnit.MILLISECONDS);
        }
    }

    /**
     * A 'template method' that concrete subclasses must implement to sense
     * something.
     * 
     * @return
     * @throws IOException
     */
    abstract public T sense() throws IOException;

    public void shutdown() {
        executorService.shutdownNow();
    }

    /**
     * Adds a callback to this sensor.
     *
     * @param callback
     */
    public void addSensorCallback(Consumer<T> callback) {
        callbacks.add(new Callback(callback, d -> true));
    }

    /**
     * Adds a callback to this sensor.
     * 
     * @param callback
     * @param filter
     */
    public void addSensorCallback(Consumer<T> callback, Predicate<T> filter) {
        callbacks.add(new Callback(callback, filter));
    }

    private RingBuffer<T> buffer;
    private Function<List<T>, T> denoiseFunction;

    /**
     * Enables noise reduction.
     * 
     * @param bufferSize
     *            the size of the buffer
     * @param denoiseFunction
     *            the function that reduces the buffer to a single value of T
     */
    public void enableNoiseReduction(int bufferSize, Function<List<T>, T> denoiseFunction) {
        this.buffer = new RingBuffer<>(bufferSize);
        this.denoiseFunction = denoiseFunction;
    }

}