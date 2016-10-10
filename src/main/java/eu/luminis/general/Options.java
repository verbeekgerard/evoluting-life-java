package eu.luminis.general;

public class Options {
	
	public static Option mainLoopSleep = new Option(0);

	public static Option mutationFraction = new Option(0.005);
	
	// LifeGene
	public static Option minOldAge = new Option(3000);
	public static Option maxOldAge = new Option(4000);
	public static Option oldAgeMutationRate = new Option(0.05);

	// Plant
	public static Option minFoodSize = new Option(4);
	public static Option maxFoodSize = new Option(10);
	public static Option minGrowthPercentage = new Option(0.01);
	public static Option maxGrowthPercentage = new Option(0.05);

	// Animal
	public static Option sizeOption = new Option(12);
	public static Option initialEnergyOption = new Option(8);
	public static Option populationSize = new Option(16*8);
	public static Option linearFrictionOption = new Option(0.06); // 0.065 // 0.024
	public static Option angularFrictionOption = new Option(0.09); // 0.25 // 0.08

	// SensorGene
	public static Option minViewDistance = new Option(16 * 10);
	public static Option maxViewDistance = new Option(16 * 12);
	public static Option minFieldOfView = new Option(Math.PI / 32);
	public static Option maxFieldOfView = new Option(Math.PI);
	public static Option viewDistanceMutationRate = new Option(0.05);
	public static Option viewDistanceReplacementRate = new Option(0.005);
	public static Option fieldOfViewMutationRate = new Option(0.05);
	public static Option fieldOfViewReplacementRate = new Option(0.005);
	
	// MovementGene
	public static double calculateForce(double maxVelocity, double friction){
        return maxVelocity * friction;
	}
	
	public static Option linearFriction = new Option(0.06); // 0.065 // 0.024
	public static Option angularFriction = new Option(0.09); // 0.25 // 0.08

	private static double minA = calculateForce(0.5, angularFriction.get());
	private static double maxA = calculateForce(5.0, angularFriction.get());
	private static double minL = calculateForce(5.0, linearFriction.get());
	private static double maxL = calculateForce(50.0,linearFriction.get());
	
	public static Option minAngularForce = new Option(minA);
	public static Option maxAngularForce = new Option(maxA);
	public static Option minLinearForce = new Option(minL);
	public static Option maxLinearForce = new Option(maxL);

	public static Option angularForceMutationRate = new Option(0.15);
	public static Option angularForceReplacementRate = new Option(0.005);
	public static Option linearForceMutationRate = new Option(0.15);
	public static Option linearForceReplacementRate = new Option(0.005);
		
	// BrainGene
	public static Option minHiddenLayers = new Option(1);
	public static Option maxHiddenLayers = new Option(5);
	public static Option maxNeuronsPerLayer = new Option(16);
	public static Option layerMutationRate = new Option(0.01);    // adding or removing a gene
	public static Option geneMutationRate = new Option(0.3);    // percentual chance of genes within a genome to mutate
	public static Option geneReplacementRate = new Option(0.001);  // completely replacing a genes properties
	
	// NeuronGene
	public static Option minThreshold = new Option(0.1);
	public static Option maxThreshold = new Option(1.0);
	public static Option maxRelaxation = new Option(99.0);

	public static Option thresholdMutationRate = new Option(0.05);
	public static Option thresholdReplacementRate = new Option(0.005);
	public static Option relaxationMutationRate = new Option(0.05);
	public static Option relaxationReplacementRate = new Option(0.005);
	
	// AxonGene
	public static Option maxStrength = new Option(0.8);
	public static Option minStrengthening = new Option(0.000001);
	public static Option maxStrengthening = new Option(0.00002);
	public static Option minWeakening = new Option(0.000001);
	public static Option maxWeakening = new Option(0.000005);
	public static Option strengthMutationRate = new Option(0.05);
	public static Option strengthReplacementRate = new Option(0.005);
	public static Option strengtheningMutationRate = new Option(0.05);
	public static Option strengtheningReplacementRate = new Option(0.005);
	public static Option weakeningMutationRate = new Option(0.05);
	public static Option weakeningReplacementRate = new Option(0.005);
}