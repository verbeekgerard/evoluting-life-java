package eu.luminis;

import eu.luminis.util.Option;

public final class Options {

    private Options() {}
	
	public final static Option mainLoopSleep = new Option(0);

	// Evolution
	public final static Option mutationFraction = new Option(0.01);
	public final static Option minMutationFraction = new Option(0.0001);
	public final static Option maxMutationFraction = new Option(0.01);
	public final static Option mutationFractionModificationPeriod = new Option(100);
	public final static Option mutationFractionExponent = new Option(-1.0/1000000.0);

	// LifeGene
    public final static Option minOldAge = new Option(3000);
    public final static Option maxOldAge = new Option(4000);
    public final static Option oldAgeMutationRate = new Option(0.05);

	// RoundSimObstacle
	public final static Option minRoundObstacleSize = new Option(6);
	public final static Option maxRoundObstacleSize = new Option(14);
	public final static Option minGrowthPercentage = new Option(0.00001);
	public final static Option maxGrowthPercentage = new Option(0.00005);
	public final static Option roundObstaclePopulationSize = new Option(10*8);

	// SimRobot
	public final static Option sizeOption = new Option(12);
	public final static Option initialEnergy = new Option(8);
	public final static Option populationSize = new Option(16*8);

	// SensorGene
	public final static Option minViewDistance = new Option(5 * 8);
	public final static Option maxViewDistance = new Option(15 * 8);
	public final static Option minFieldOfView = new Option(Math.PI * 0.6);
	public final static Option maxFieldOfView = new Option(Math.PI);
	public final static Option viewDistanceMutationRate = new Option(0.1);
	public final static Option viewDistanceReplacementRate = new Option(0.05);
	public final static Option fieldOfViewMutationRate = new Option(0.05);
	public final static Option fieldOfViewReplacementRate = new Option(0.005);
	
	// MovementGene
	private static double calculateForce(double maxVelocity, double friction){
        return maxVelocity * friction;
	}
	
	public final static Option linearFriction = new Option(0.06); // 0.06
	public final static Option angularFriction = new Option(0.6); // 0.5

	private static double minA = calculateForce(0.3, angularFriction.get()); // 0.1
	private static double maxA = calculateForce(2.0, angularFriction.get()); // 1.0
	private static double minL = calculateForce(5.0, linearFriction.get());
	private static double maxL = calculateForce(12.0,linearFriction.get());
	
	public final static Option minAngularForce = new Option(minA);
	public final static Option maxAngularForce = new Option(maxA);
	public final static Option minLinearForce = new Option(minL);
	public final static Option maxLinearForce = new Option(maxL);

	public final static Option angularForceMutationRate = new Option(0.25);
	public final static Option angularForceReplacementRate = new Option(0.01);
	public final static Option linearForceMutationRate = new Option(0.15);
	public final static Option linearForceReplacementRate = new Option(0.005);
		
	// BrainGene
	public final static Option minHiddenLayers = new Option(2);
	public final static Option maxHiddenLayers = new Option(4);
	public final static Option maxNeuronsPerLayer = new Option(16);
	public final static Option layerMutationRate = new Option(0.01);    // adding or removing a neuron
	public final static Option neuronMutationRate = new Option(0.2);
	public final static Option neuronReplacementRate = new Option(0.001);
	
	// NeuronGene
	public final static Option minThreshold = new Option(0.1);
	public final static Option maxThreshold = new Option(1.0);
	public final static Option maxRelaxation = new Option(99.0);

	public final static Option thresholdMutationRate = new Option(0.05);
	public final static Option thresholdReplacementRate = new Option(0.005);
	public final static Option relaxationMutationRate = new Option(0.05);
	public final static Option relaxationReplacementRate = new Option(0.005);
	
	// AxonGene
	public final static Option maxStrength = new Option(0.8);
	public final static Option minStrengthening = new Option(0.000001);
	public final static Option maxStrengthening = new Option(0.00002);
	public final static Option minWeakening = new Option(0.000001);
	public final static Option maxWeakening = new Option(0.000005);
	public final static Option strengthMutationRate = new Option(0.05);
	public final static Option strengthReplacementRate = new Option(0.005);
	public final static Option strengtheningMutationRate = new Option(0.05);
	public final static Option strengtheningReplacementRate = new Option(0.005);
	public final static Option weakeningMutationRate = new Option(0.05);
	public final static Option weakeningReplacementRate = new Option(0.005);
}