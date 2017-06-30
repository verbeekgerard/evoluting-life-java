package eu.luminis;

import eu.luminis.events.EventBroadcaster;
import eu.luminis.evolution.CycleCostFactorModifier;
import eu.luminis.export.StatisticsExporter;
import eu.luminis.evolution.MutationFractionModifier;
import eu.luminis.ui.*;

import java.util.Arrays;

public class Run {

	/**
	 * @param args
	 * headless:	'headless'
     * rnn:         'rnn'
	 */
	public static void main(String[] args) {
		boolean visible = getVisible(args);
		Options.brainIsRecurrent = getRecurrentNeuralNetwork(args);

        System.out.println("Start simulation with " + (Options.brainIsRecurrent ?
                "recurrent neural networks" :
                "feed forward neural networks"));

        Simulation simulation = Simulation.getInstance();
		EventBroadcaster eventBroadcaster = EventBroadcaster.getInstance();
		
		if(visible){
            initializeCanvas(simulation, eventBroadcaster);
            initializeMainPanel(simulation, eventBroadcaster);
		}

        initializeMutationFractionModifier(eventBroadcaster);
        initializeCycleCostFactorModifier(simulation, eventBroadcaster);
        initializeStatsPrinter(eventBroadcaster);

		simulation.startMainLoop();
	}

    private static void initializeMutationFractionModifier(EventBroadcaster eventBroadcaster) {
        MutationFractionModifier mutationFractionModifier = new MutationFractionModifier();
        eventBroadcaster.addObserver(mutationFractionModifier);
    }

    private static void initializeCycleCostFactorModifier(Simulation simulation, EventBroadcaster eventBroadcaster) {
        CycleCostFactorModifier observer = new CycleCostFactorModifier(simulation.getWorld().getRobotPopulation());
        eventBroadcaster.addObserver(observer);
    }

    private static void initializeStatsPrinter(EventBroadcaster eventBroadcaster) {
        StatsCollector statsCollector = StatsCollectorBuilder.statsCollector()
                .build();
        eventBroadcaster.addObserver(new StatsPrinter(statsCollector));
    }

    private static void initializeMainPanel(Simulation simulation, EventBroadcaster eventBroadcaster) {
        StatsCollector statsCollector = StatsCollectorBuilder.statsCollector()
                .build();
        MainPanel mainPanel = new MainPanel(statsCollector, simulation);
        eventBroadcaster.addObserver(mainPanel);
    }

    private static void initializeCanvas(Simulation simulation, EventBroadcaster eventBroadcaster) {
        Canvas canvas = new Canvas(simulation.getWorld());
        eventBroadcaster.addObserver(canvas);
    }

    private static boolean getVisible(String[] args){
        return Arrays.asList(args).indexOf("headless") == -1;
    }

    private static boolean getRecurrentNeuralNetwork(String[] args){
        return Arrays.asList(args).indexOf("rnn") > -1;
    }
}