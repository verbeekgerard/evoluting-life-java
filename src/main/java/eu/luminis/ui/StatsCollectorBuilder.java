package eu.luminis.ui;

import eu.luminis.Simulation;
import eu.luminis.events.EventBroadcaster;

/**
 * Build up a StatsCollector
 */
public class StatsCollectorBuilder {
    private Simulation simulation;
    EventBroadcaster eventBroadcaster;

    private StatsCollectorBuilder() {
        simulation = Simulation.getInstance();
        eventBroadcaster = EventBroadcaster.getInstance();
    }

    public static StatsCollectorBuilder statsCollector() {
        return new StatsCollectorBuilder();
    }

    public StatsCollector build() {
        StatsCollector statsCollector = new StatsCollector(simulation.getWorld().getRobotPopulation());
        eventBroadcaster.addObserver(statsCollector);

        return statsCollector;
    }
}
