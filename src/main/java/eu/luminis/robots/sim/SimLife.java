package eu.luminis.robots.sim;

class SimLife implements IAgeRetriever {
    private final int maxAge;
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

    @Override
    public Integer getAge() {
        return age;
    }

    @Override
    public int getOldAge() {
        return maxAge;
    }
}
