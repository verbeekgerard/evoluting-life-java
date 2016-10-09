package eu.luminis.ui;

class PeriodicStats {

    private double avgHealth;
    private double avgAge;
    private double avgDistance;
    private double bestFitness;

    public PeriodicStats(double avgHealth, double avgAge, double avgDistance, double bestFitness) {
        this.avgHealth = avgHealth;
        this.avgAge = avgAge;
        this.avgDistance = avgDistance;
        this.bestFitness = bestFitness;
    }

    public double getAverageHealth() {
        return avgHealth;
    }

    public double getAverageAge() {
        return avgAge;
    }

    public double getAverageDistance() {
        return avgDistance;
    }

    public double getBestFitness() {
        return bestFitness;
    }
}