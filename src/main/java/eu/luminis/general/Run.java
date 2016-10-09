package eu.luminis.general;

import eu.luminis.export.ExportInfoImpl;
import eu.luminis.ui.Canvas;
import eu.luminis.ui.StatsCollector;
import eu.luminis.ui.MainPanel;
import eu.luminis.ui.StatsPrinter;

public class Run {

	/**
	 * @param args
	 * 0:	visible:	'true' or 'false'
	 */
	public static void main(String[] args) {
		boolean visible = getVisible(args);

		General general = General.getInstance();
		EventBroadcaster eventBroadcaster = EventBroadcaster.getInstance();
		
		if(visible == true){
            initializeCanvas(general, eventBroadcaster);
            initializeMainPanel(general, eventBroadcaster);
		}

        initializeCostCalculator(eventBroadcaster);
        initializeStatsPrinter(general, eventBroadcaster);
        initializeExportInfo(general, eventBroadcaster);

		general.startMainLoop();
	}

    private static void initializeCostCalculator(EventBroadcaster eventBroadcaster) {
        eventBroadcaster.addObserver(CostCalculator.getInstance());
    }

    private static void initializeExportInfo(General general, EventBroadcaster eventBroadcaster) {
        StatsCollector statsCollector = createStatsCollectorInstance(general, eventBroadcaster);
        ExportInfoImpl.create(statsCollector);
    }

    private static void initializeStatsPrinter(General general, EventBroadcaster eventBroadcaster) {
        StatsCollector statsCollector = createStatsCollectorInstance(general, eventBroadcaster);
        eventBroadcaster.addObserver(new StatsPrinter(statsCollector));
    }

    private static void initializeMainPanel(General general, EventBroadcaster eventBroadcaster) {
        StatsCollector statsCollector = createStatsCollectorInstance(general, eventBroadcaster);
        MainPanel mainPanel = new MainPanel(statsCollector, general);
        eventBroadcaster.addObserver(mainPanel);
    }

    private static void initializeCanvas(General general, EventBroadcaster eventBroadcaster) {
        Canvas canvas = new Canvas(general.getFoodSupply(), general.getPopulation(), general.getWorld());
        eventBroadcaster.addObserver(canvas);
    }

	private static StatsCollector createStatsCollectorInstance(General general, EventBroadcaster eventBroadcaster) { // TODO: move this to a factory
		StatsCollector statsCollector = new StatsCollector(general.getPopulation());
        eventBroadcaster.addObserver(statsCollector);

		return statsCollector;
	}

    private static boolean getVisible(String[] args){
        boolean visual = true;
        if(args.length > 0){
            visual = new Boolean((args[0]));
        }
        return visual;
    }
}