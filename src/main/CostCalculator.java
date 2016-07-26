package main;

public class CostCalculator {

	public final static double ENERGY_COST = 0.001;

	public static double cycle() {
		return ENERGY_COST * 30;
	}

	public static double accelerate(double acceleration) {
		return ENERGY_COST * Math.abs(acceleration);
	}

	public static double rotate(double acceleration) {
		return ENERGY_COST * Math.abs(acceleration);
	}
	
	public static double collide(double velocity) {
		return ENERGY_COST * Math.abs(velocity) * 50;
	}
	
	public static double travelledDistance(double distance) {
		return ENERGY_COST * Math.abs(distance) * 500;
	}
}
			
