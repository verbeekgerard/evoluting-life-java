package General;

import java.util.Observable;
import java.util.Observer;

public class CostCalculator implements Observer {

	private final static CostCalculator instance = new CostCalculator();
	private final static double ENERGY_COST = 0.001;

	public static CostCalculator getInstance() {
		return instance;
	}
	
	private int iteration = 0;

	public double cycle() {
		return ENERGY_COST * 40;
	}

	public double accelerate(double acceleration) {
		return ENERGY_COST * Math.abs(acceleration);
	}

	public double rotate(double acceleration) {
		return ENERGY_COST * Math.abs(acceleration);
	}
	
	public double collide(double velocity) {
		return ENERGY_COST * Math.abs(velocity) * 20000;// this.iteration / 100;
	}
	
	public double travelledDistance(double distance) {
		return ENERGY_COST * Math.abs(distance) * 50;
	}

	@Override
	public void update(Observable o, Object arg) {
		Event event = (Event) arg;
		if (event.type.equals(EventType.CYCLE_END)) {
			this.iteration++;
		}
	}
}