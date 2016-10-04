package eu.luminis.ui;

import java.text.DecimalFormat;
import java.util.List;

public class Stats {

    private int totalStarved;
    private int totalCollisions;
    private int totalWandered;
    private int totalDiedOfAge;

    private double avgHealth;
    private double avgAge;
    private double avgDistance;
    private double avgBestFitness;

    DecimalFormat df2 = new DecimalFormat("#.##");

    public Stats(int totalStarved, int totalCollisions, int totalWandered, int totalDiedOfAge, List<PeriodicStats> periodicStatsList) {
        this.totalStarved = totalStarved;
        this.totalCollisions = totalCollisions;
        this.totalWandered = totalWandered;
        this.totalDiedOfAge = totalDiedOfAge;

        double totalHealth = 0;
        double totalAge = 0;
        double totalDistance = 0;
        double totalBestFitness = 0;

        for (PeriodicStats periodicStats : periodicStatsList) {
            totalHealth += periodicStats.getAverageHealth();
            totalAge += periodicStats.getAverageAge();
            totalDistance += periodicStats.getAverageDistance();
            totalBestFitness += periodicStats.getBestFitness();
        }

        avgHealth = totalHealth / periodicStatsList.size();
        avgAge = totalAge / periodicStatsList.size();
        avgDistance = totalDistance / periodicStatsList.size();
        avgBestFitness = totalBestFitness / periodicStatsList.size();
    }

    public int getTotalDiedOfAge() {
        return totalDiedOfAge;
    }
    public int getTotalWandered() {
        return totalWandered;
    }
    public int getTotalCollisions() {
        return totalCollisions;
    }
    public int getTotalStarved() {
        return totalStarved;
    }

    public double getAverageHealth() {
        return avgHealth;
    }
    public double getAverageAge() { return avgAge; }
    public double getAverageDistance() {
        return avgDistance;
    }
    public double getAverageBestFitness() {
        return avgBestFitness;
    }

    public String getAverageHealthString() {
        return df2.format(avgHealth);
    }
    public String getAverageAgeString() { return df2.format(avgAge); }
    public String getAverageDistanceString() { return df2.format(avgDistance); }
    public String getAverageBestFitnessString() { return df2.format(avgDistance); }

    @Override
    public String toString() {
        return
                "avg. health:\t" + getAverageHealthString() + "\t" +
                "avg. age:\t" + getAverageAgeString() + "\t" +
                "avg. distance:\t" + getAverageDistanceString() + "\t" +
                "totalCollisions:\t" + totalCollisions + "\t" +
//                "totalStarved:\t" + totalStarved + "\t" +
//                "totalWandered:\t" + totalWandered + "\t" +
                "totalDiedOfAge:\t" + totalDiedOfAge + "\t" +
                "best:\t" + getAverageBestFitnessString() + "\t";
    }
}
