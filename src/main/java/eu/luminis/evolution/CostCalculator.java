package eu.luminis.evolution;

public final class CostCalculator {

	private final static double ENERGY_COST = 0.001;
	private final static CostCalculator instance = new CostCalculator();

	public static CostCalculator getInstance() {
		return instance;
	}

	public double cycle() {
		return ENERGY_COST * 35;
	}

	public double accelerate(double acceleration) {
		return ENERGY_COST * Math.abs(acceleration);
	}

	public double collide(double velocity) {
		return ENERGY_COST * (Math.abs(velocity) + 0.01) * 20000;
	}

	public double distanceReward(double distance) {
		return ENERGY_COST * Math.abs(distance) * 50;
	}

	public double turnHead(double angularAcceleration) {
		return ENERGY_COST * Math.abs(angularAcceleration) * 5;
	}
}