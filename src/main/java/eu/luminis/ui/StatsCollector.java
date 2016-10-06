package eu.luminis.ui;

import eu.luminis.entities.Animal;
import eu.luminis.general.Event;
import eu.luminis.general.EventType;
import eu.luminis.general.Population;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class StatsCollector implements Observer {

    private int totalStarved;
    private int totalCollisions;
    private int totalWandered;
    private int totalDiedOfAge;

    private Population population;
    private Stats stats;
    private List<PeriodicStats> periodicStatsList = new ArrayList<>();

    private static StatsCollector instance = null;

    private StatsCollector(Population population) {
        this.population = population;
    }

    public static StatsCollector getInstance(Population population){
        if(instance == null){
            instance = new StatsCollector(population);
        }
        return instance;
    }

    protected void collectPeriodicStats() {
        double totalHealth = 0;
        double totalAge = 0;
        double totalDistance = 0;

        for (Animal animal : population.entities) {
            totalHealth += animal.getHealth();
            totalAge += animal.getAge();
            totalDistance += animal.getTravelledDistance();
        }

        double avgHealth = totalHealth / population.entities.size();
        double avgAge = totalAge / population.entities.size();
        double avgDistance = totalDistance / population.entities.size();
        double bestFitness = population.winningEntity.fitness();

        PeriodicStats periodicStats = new PeriodicStats(avgHealth, avgAge, avgDistance, bestFitness);

        periodicStatsList.add(periodicStats);
    }

    protected void collectStats() {
        stats = new Stats(totalStarved, totalCollisions, totalWandered, totalDiedOfAge, periodicStatsList);
        resetStats();
    }

    public void resetStats() {
        this.periodicStatsList = new ArrayList<>();
        this.totalCollisions = 0;
        this.totalStarved = 0;
        this.totalWandered = 0;
        this.totalDiedOfAge = 0;
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
        collectStats();
        return stats;
    }
}
