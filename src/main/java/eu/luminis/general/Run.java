package eu.luminis.general;

import eu.luminis.ui.Canvas;
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
		
		JFrame frame = new JFrame("Evoluting-life-java");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Canvas canvas = new Canvas(general.foodSupply, general.population);
	    frame.add(canvas);
	    frame.setSize(new Double(general.world.width).intValue(), new Double(general.world.height + 20).intValue());
	    frame.setVisible(visible);
	    frame.setResizable(false);
		
		general.addObserver(CostCalculator.getInstance());
		general.addObserver(canvas);
		general.addObserver(new StatsPrinter(general.foodSupply, general.population));
		
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