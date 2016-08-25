package eu.luminis.general;

import eu.luminis.ui.Canvas;
import eu.luminis.ui.StatsCollector;
import eu.luminis.ui.StatsPrinter;

import javax.swing.*;

public class Run {

	/**
	 *
	 * @param args
	 * 0:	visible:	'true' or 'false'
	 */
	public static void main(String[] args) {
		boolean visible = getVisible(args);

		General general = General.getInstance();
		
	    Canvas canvas = new Canvas(general.foodSupply, general.population);
		if(visible == true){
			JFrame frame = new JFrame("Evoluting-life-java");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(canvas);
			frame.setSize(new Double(general.world.width).intValue(), new Double(general.world.height + 20).intValue());
			frame.setVisible(true);
			frame.setResizable(false);
		}

		general.addObserver(CostCalculator.getInstance());
		general.addObserver(canvas);

		StatsCollector statsCollector = StatsCollector.getInstance(general.population);
		general.addObserver(new StatsPrinter(statsCollector));
		general.addObserver(statsCollector);

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