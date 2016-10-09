package eu.luminis.ui;

class FrameLimiter {
    private long limitNanoTime;
    private long previousNanoTime = 0;

    public FrameLimiter(int fps) {
        limitNanoTime = 1000000000 / fps;
    }

    public boolean isNewFrame() {
        long currentNanoTime = System.nanoTime();
        long deltaNanoTime = currentNanoTime - previousNanoTime;

        if (deltaNanoTime <= limitNanoTime) {
            return false;
        }

        previousNanoTime = currentNanoTime;
        return true;
    }
}
