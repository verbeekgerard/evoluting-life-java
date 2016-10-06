package eu.luminis.general;

import eu.luminis.export.ExportInfoImpl;
import eu.luminis.ui.Canvas;
import eu.luminis.ui.StatsCollector;
import eu.luminis.ui.StatsPanel;
import eu.luminis.ui.StatsPrinter;

public class Run {

	/**
	 * @param args
	 * 0:	visible:	'true' or 'false'
	 */
	public static void main(String[] args) {
		boolean visible = getVisible(args);

		General general = General.getInstance();
		
		if(visible == true){
			Canvas canvas = new Canvas(general.foodSupply, general.population, general.world);
			general.addObserver(canvas);
			StatsPanel statsPanel = new StatsPanel(createStatsCollectorInstance(), general);
			general.addObserver(statsPanel);
		}

		general.addObserver(CostCalculator.getInstance());
		general.addObserver(new StatsPrinter(createStatsCollectorInstance()));

		ExportInfoImpl.create(createStatsCollectorInstance());

		general.startMainLoop();
	}


	private static boolean getVisible(String[] args){
		boolean visual = true;
			if(args.length > 0){
				visual = new Boolean((args[0]));
			}
		return visual;
	}

	private static StatsCollector createStatsCollectorInstance() { // TODO: move this to a factory
		General general = General.getInstance();
		StatsCollector statsCollector = new StatsCollector(general.population);
		general.addObserver(statsCollector);

		return statsCollector;
	}
}