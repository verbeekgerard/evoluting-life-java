package General;

import javax.swing.JFrame;

import ui.Canvas;
import ui.StatsPrinter;

public class Run {

	public static void main(String[] args) {
		
		General general = General.getInstance();
		
		JFrame frame = new JFrame("Evoluting-life-java");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Canvas canvas = new Canvas(general.foodSupply, general.population);
	    frame.add(canvas);
	    frame.setSize(new Double(general.world.width).intValue(), new Double(general.world.height + 20).intValue());
	    frame.setVisible(true);
	    frame.setResizable(false);
		
		general.addObserver(CostCalculator.getInstance());
		general.addObserver(canvas);
		general.addObserver(new StatsPrinter(general.foodSupply, general.population));
		
		general.startMainLoop();
	}
}