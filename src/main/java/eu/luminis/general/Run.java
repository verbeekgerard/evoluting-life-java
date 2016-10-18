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

		Simulation simulation = Simulation.getInstance();
		EventBroadcaster eventBroadcaster = EventBroadcaster.getInstance();
		
		if(visible){
            initializeCanvas(simulation, eventBroadcaster);
            initializeMainPanel(simulation, eventBroadcaster);
		}

        initializeMutationFractionModifier(eventBroadcaster);
        initializeStatsPrinter(simulation, eventBroadcaster);
        initializeExportInfo(simulation, eventBroadcaster);

		simulation.startMainLoop();
	}

    private static void initializeMutationFractionModifier(EventBroadcaster eventBroadcaster) {
        MutationFractionModifier mutationFractionModifier = new MutationFractionModifier();
        eventBroadcaster.addObserver(mutationFractionModifier);
    }

    private static void initializeExportInfo(Simulation simulation, EventBroadcaster eventBroadcaster) {
        StatsCollector statsCollector = createStatsCollectorInstance(simulation, eventBroadcaster);
        ExportInfoImpl.create(statsCollector);
    }

    private static void initializeStatsPrinter(Simulation simulation, EventBroadcaster eventBroadcaster) {
        StatsCollector statsCollector = createStatsCollectorInstance(simulation, eventBroadcaster);
        eventBroadcaster.addObserver(new StatsPrinter(statsCollector));
    }

    private static void initializeMainPanel(Simulation simulation, EventBroadcaster eventBroadcaster) {
        StatsCollector statsCollector = createStatsCollectorInstance(simulation, eventBroadcaster);
        MainPanel mainPanel = new MainPanel(statsCollector, simulation);
        eventBroadcaster.addObserver(mainPanel);
    }

    private static void initializeCanvas(Simulation simulation, EventBroadcaster eventBroadcaster) {
        Canvas canvas = new Canvas(simulation.getStationaryObstaclePopulation(), simulation.getRobotPopulation(), simulation.getWorld());
        eventBroadcaster.addObserver(canvas);
    }

	private static StatsCollector createStatsCollectorInstance(Simulation simulation, EventBroadcaster eventBroadcaster) { // TODO: move this to a factory
		StatsCollector statsCollector = new StatsCollector(simulation.getRobotPopulation());
        eventBroadcaster.addObserver(statsCollector);

		return statsCollector;
	}

    private static boolean getVisible(String[] args){
        boolean visible = true;
        if(args.length > 0) {
            visible = Boolean.valueOf((args[0]));
        }
        return visible;
    }
}