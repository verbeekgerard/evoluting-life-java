package eu.luminis.evolution;

import eu.luminis.Options;
import eu.luminis.util.Option;

public final class CostCalculator {

	private final static double ENERGY_COST = 0.001;
	private final static CostCalculator instance = new CostCalculator();

	public static CostCalculator getInstance() {
		return instance;
	}

	private static final Option cycleCostFactor = Options.cycleCostFactor;
	private static final Option linearAccelerationCostFactor = Options.linearAccelerationCostFactor;
	private static final Option collideCostFactor = Options.collideCostFactor;
	private static final Option distanceRewardFactor = Options.distanceRewardFactor;
	private static final Option angularAccelerationCostFactor = Options.angularAccelerationCostFactor;

	public double cycle() {
		return ENERGY_COST * cycleCostFactor.get();
	}

	public double accelerate(double acceleration) {
		return ENERGY_COST * Math.abs(acceleration) * linearAccelerationCostFactor.get();
	}

	public double collide(double velocity) {
		return ENERGY_COST * (Math.abs(velocity) + 0.001) * collideCostFactor.get();
	}

	public double distanceReward(double distance) {
		return ENERGY_COST * distance * distanceRewardFactor.get();
	}

	public double turnHead(double angularAcceleration) {
		return ENERGY_COST * Math.abs(angularAcceleration) * angularAccelerationCostFactor.get();
	}
}