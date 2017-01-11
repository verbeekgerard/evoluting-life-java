package eu.luminis.export;

import eu.luminis.ui.Stats;
import eu.luminis.ui.StatsCollector;

public class StatisticsExporter implements IStatisticsExporter {
    private static StatisticsExporter instance;
    public static StatisticsExporter create(StatsCollector statsCollector) {
        if(instance == null) {
            instance = new StatisticsExporter(statsCollector);
        }

        return instance;
    }

    public static IStatisticsExporter getInstance(){
        return instance;
    }

    private final StatsCollector statsCollector;
    private StatisticsExporter(StatsCollector statsCollector) {
        this.statsCollector = statsCollector;
    }

    @Override
    public Stats getStats() {
        return statsCollector.getStats();
    }
}