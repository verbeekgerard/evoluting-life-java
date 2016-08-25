package eu.luminis.info;

import eu.luminis.ui.StatsCollector;

/**
 * Created by gerardverbeek on 25/08/16.
 */
public class ExportInfoImpl implements ExportInfo {

    StatsCollector collector;

    public ExportInfoImpl(StatsCollector statsCollector){
        collector = statsCollector;
    }

    @Override
    public String getHealth() {
        return collector.getStats().getAvgHealth();
    }

}
