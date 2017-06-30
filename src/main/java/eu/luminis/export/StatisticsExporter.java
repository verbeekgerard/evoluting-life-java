package eu.luminis.export;

import eu.luminis.ui.Stats;
import eu.luminis.ui.StatsCollector;
import eu.luminis.ui.StatsCollectorBuilder;

public class StatisticsExporter implements IStatisticsExporter {
    private static StatisticsExporter instance;
    private final StatsCollector statsCollector;

    private StatisticsExporter() {
        this.statsCollector = StatsCollectorBuilder.statsCollector()
                .build();
    }

    public static IStatisticsExporter getInstance(){
        if(instance == null) {
            instance = new StatisticsExporter();
        }

        return instance;
    }

    @Override
    public Stats getStats() {
        return statsCollector.getStats();
    }
}