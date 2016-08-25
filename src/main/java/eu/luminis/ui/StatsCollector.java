package eu.luminis.ui;

import eu.luminis.entities.Animal;
import eu.luminis.general.Event;
import eu.luminis.general.EventType;
import eu.luminis.general.Population;

import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by gerardverbeek on 25/08/16.
 */
public class StatsCollector implements Observer {

    private int totalStarved;
    private int totalCollisions;
    private int totalWandered;
    private int totalDiedOfAge;
    private double avgHealth;
    private Population population;
    private Stats stats;

    DecimalFormat df3;
    DecimalFormat df2;

    private static StatsCollector instance = null;

    private StatsCollector(Population population) {
        this.population = population;
        df3 = new DecimalFormat("#.###");
        df2 = new DecimalFormat("#.##");
    }

    public static StatsCollector getInstance(Population population){
        if(instance == null){
            instance = new StatsCollector(population);
        }
        return instance;
    }

    protected void collectStats() {
        double totalHealth = 0;
        for (Animal animal : population.entities) {
            totalHealth += animal.getHealth();
        }
        this.avgHealth = totalHealth / population.entities.size();
        stats = new Stats(totalStarved,totalCollisions,totalWandered,totalDiedOfAge,getAvgHealth(), getBest());
        resetStats();

    }

    public void resetStats() {
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
            if ((int)event.value % 100 == 0) {
                collectStats();
            }
        }
    }

    private String getBest(){
        return df3.format(population.winningEntity.rank());
    }

    public Stats getStats(){
        return stats;
    }

    public String getAvgHealth() {
        return df2.format(avgHealth);
    }
}
