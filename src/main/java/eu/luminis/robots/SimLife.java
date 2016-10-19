package eu.luminis.robots;

public class SimLife {
    private int maxAge;
    private int age;

    public SimLife(int maxAge) {
        this.maxAge = maxAge;
    }

    public void recordLifeCycle() {
        age++;
    }

    public boolean isOverOldAge() {
        return age > maxAge;
    }
}
