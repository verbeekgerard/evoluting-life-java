package eu.luminis.ui;

import eu.luminis.general.Event;
import eu.luminis.general.EventType;
import eu.luminis.robots.sim.SimRobot;
import eu.luminis.robots.sim.SimRobotPopulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class StatsCollector implements Observer {

    private int totalStarved;
    private int totalCollisions;
    private int totalWandered;
    private int totalDiedOfAge;

    private SimRobotPopulation population;
    private Stats stats;
    private List<PeriodicStats> periodicStatsList = new ArrayList<>();

    public StatsCollector(SimRobotPopulation population) {
        this.population = population;
    }

    @Override
    public void update(Observable o, Object arg) {
        Event event = (Event) arg;

        if (event.type.equals(EventType.COLLIDE)) {
            totalCollisions++;
        }
        else if (event.type.equals(EventType.STARVED)) {
            totalStarved++;
        }
        else if (event.type.equals(EventType.WANDERED)) {
            totalWandered++;
        }
        else if (event.type.equals(EventType.DIED_OF_AGE)) {
            totalDiedOfAge++;
        }
        else if (event.type.equals(EventType.CYCLE_END)) {
            collectPeriodicStats();
        }
    }

    public Stats getStats(){
        stats = new Stats(totalStarved, totalCollisions, totalWandered, totalDiedOfAge, periodicStatsList);
        resetStats();
        return stats;
    }

    private void collectPeriodicStats() {
        double totalHealth = 0;
        double totalAge = 0;
        double totalDistance = 0;

        for (SimRobot robot : population.getAllRobots()) {
            totalHealth += robot.fitness();
            totalAge += robot.getAgeInformation().getAge();
            totalDistance += robot.getTravelledDistance();
        }

        double avgHealth = totalHealth / population.getAllRobots().size();
        double avgAge = totalAge / population.getAllRobots().size();
        double avgDistance = totalDistance / population.getAllRobots().size();
        double bestFitness = population.getWinningEntity().fitness();

        PeriodicStats periodicStats = new PeriodicStats(avgHealth, avgAge, avgDistance, bestFitness);

        periodicStatsList.add(periodicStats);
    }

    private void resetStats() {
        this.periodicStatsList = new ArrayList<>();
        this.totalCollisions = 0;
        this.totalStarved = 0;
        this.totalWandered = 0;
        this.totalDiedOfAge = 0;
    }
}
