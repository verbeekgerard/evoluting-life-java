package eu.luminis.ui;

public class Stats {

    private int totalStarved;
    private int totalCollisions;
    private int totalWandered;
    private int totalDiedOfAge;
    private String avgHealth;
    private String best;

    public Stats(int totalStarved, int totalCollisions, int totalWandered, int totalDiedOfAge, String avgHealth, String best) {
        this.totalStarved = totalStarved;
        this.totalCollisions = totalCollisions;
        this.totalWandered = totalWandered;
        this.totalDiedOfAge = totalDiedOfAge;
        this.avgHealth = avgHealth;
        this.best = best;
    }

    public String getBest() {
        return best;
    }
    public void setBest(String best) {
        this.best = best;
    }
    public String getAvgHealth() {
        return avgHealth;
    }
    public void setAvgHealth(String avgHealth) {
        this.avgHealth = avgHealth;
    }
    public int getTotalDiedOfAge() {
        return totalDiedOfAge;
    }
    public void setTotalDiedOfAge(int totalDiedOfAge) {
        this.totalDiedOfAge = totalDiedOfAge;
    }
    public int getTotalWandered() {
        return totalWandered;
    }
    public void setTotalWandered(int totalWandered) {
        this.totalWandered = totalWandered;
    }
    public int getTotalCollisions() {
        return totalCollisions;
    }
    public void setTotalCollisions(int totalCollisions) {
        this.totalCollisions = totalCollisions;
    }
    public int getTotalStarved() {
        return totalStarved;
    }
    public void setTotalStarved(int totalStarved) {
        this.totalStarved = totalStarved;
    }


    @Override
    public String toString() {
        return
                "avg. health:\t" + avgHealth + "\t" +
                "totalCollisions:\t" + totalCollisions + "\t" +
                "totalStarved:\t" + totalStarved + "\t" +
                "totalWandered:\t" + totalWandered + "\t" +
                "totalDiedOfAge:\t" + totalDiedOfAge + "\t" +
                "best:\t" + best + "\t";
    }
}
