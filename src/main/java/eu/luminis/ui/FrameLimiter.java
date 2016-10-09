package eu.luminis.ui;

public class FrameLimiter {
    private long limitNanoTime;
    private long previousNanoTime = 0;

    public FrameLimiter(int fps) {
        limitNanoTime = 1000000000 / fps;
    }

    public boolean isNewFrame() {
        long currentNanoTime = System.nanoTime();
        long deltaNanoTime = currentNanoTime - previousNanoTime;

        if (deltaNanoTime > limitNanoTime) {
            previousNanoTime = currentNanoTime;
            return true;
        }

        return false;
    }
}
