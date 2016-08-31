package eu.luminis.export;

import eu.luminis.ui.Stats;
import eu.luminis.ui.StatsCollector;

/**
 * Created by gerardverbeek on 25/08/16.
 */
public class ExportInfoImpl implements ExportInfo {

    private static ExportInfoImpl instance;
    private static StatsCollector collector;

    private ExportInfoImpl(){}

    public static ExportInfoImpl create(StatsCollector statsCollector){
        if(instance == null){
            instance = new ExportInfoImpl();
            collector = statsCollector;
        }
        return instance;
    }

    public static ExportInfo getInstance(){
        return instance;
    }


    @Override
    public String getHealth() {
        return collector.getStats().getAvgHealth();
    }

    @Override
    public Stats getStats() {
        return collector.getStats();
    }
}
