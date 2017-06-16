package eu.luminis.evolution;

import eu.luminis.Options;

public final class CostCalculator {

	private final static double ENERGY_COST = 0.001;
	private final static CostCalculator instance = new CostCalculator();

	public static CostCalculator getInstance() {
		return instance;
	}

	private static final double cycleCostFactor = Options.cycleCostFactor.get();
	private static final double linearAccelerationCostFactor = Options.linearAccelerationCostFactor.get();
	private static final double collideCostFactor = Options.collideCostFactor.get();
	private static final double distanceRewardFactor = Options.distanceRewardFactor.get();
	private static final double angularAccelerationCostFactor = Options.angularAccelerationCostFactor.get();

	public double cycle() {
		return ENERGY_COST * cycleCostFactor;
	}

	public double accelerate(double acceleration) {
		return ENERGY_COST * Math.abs(acceleration) * linearAccelerationCostFactor;
	}

	public double collide(double velocity) {
		return ENERGY_COST * (Math.abs(velocity) + 0.001) * collideCostFactor;
	}

	public double distanceReward(double distance) {
		return ENERGY_COST * distance * distanceRewardFactor;
	}

	public double turnHead(double angularAcceleration) {
		return ENERGY_COST * Math.abs(angularAcceleration) * angularAccelerationCostFactor;
	}
}