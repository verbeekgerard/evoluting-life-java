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
		
		StatsCollector statsCollector = StatsCollector.getInstance(general.population);
		
		if(visible == true){
			Canvas canvas = new Canvas(general.foodSupply, general.population, general.world);
			general.addObserver(canvas);
			StatsPanel statsPanel = new StatsPanel(statsCollector, general);
			general.addObserver(statsPanel);
		}

		general.addObserver(CostCalculator.getInstance());
		general.addObserver(new StatsPrinter(statsCollector));
		general.addObserver(statsCollector);
		ExportInfoImpl.create(statsCollector);

		general.startMainLoop();
	}


	private static boolean getVisible(String[] args){
		boolean visual = true;
			if(args.length > 0){
				visual = new Boolean((args[0]));
			}
		return visual;
	}
}