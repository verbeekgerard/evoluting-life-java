package eu.luminis.ui;

import eu.luminis.Options;

import java.text.DecimalFormat;
import java.util.List;

public class Stats {

    private int totalStarved;
    private int totalCollisions;
    private int totalWandered;
    private int totalDiedOfAge;

    private double averageHealth;
    private double averageAge;
    private double averageDistance;
    private double averageBestFitness;

    private DecimalFormat df2 = new DecimalFormat("0.00");

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

        averageHealth = totalHealth / periodicStatsList.size();
        averageAge = totalAge / periodicStatsList.size();
        averageDistance = totalDistance / periodicStatsList.size();
        averageBestFitness = totalBestFitness / periodicStatsList.size();
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
        return averageHealth;
    }
    public double getAverageAge() { return averageAge; }
    public double getAverageDistance() { return averageDistance; }
    public double getAverageBestFitness() {
        return averageBestFitness;
    }
    public double getMutationFraction() { return Options.mutationFraction.get(); }

    @Override
    public String toString() {
        return
                "avg. health:\t" + df2.format(averageHealth) + "\t" +
                "avg. age:\t" + df2.format(averageAge) + "\t" +
                "avg. distance:\t" + df2.format(averageDistance) + "\t" +
                "totalCollisions:\t" + totalCollisions + "\t" +
//                "totalStarved:\t" + totalStarved + "\t" +
//                "totalWandered:\t" + totalWandered + "\t" +
//                "totalDiedOfAge:\t" + totalDiedOfAge + "\t" +
                "best:\t" + df2.format(averageBestFitness) + "\t" +
                "mutationFraction:\t" + getMutationFraction();
    }
}
