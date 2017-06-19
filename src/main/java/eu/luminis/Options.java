package eu.luminis;

import eu.luminis.util.Option;

public final class Options {

    private final static double defaultMutationRate = 0.05;
    private final static double defaultReplacementRate = 0.005;

    private Options() {
    }

    public final static Option mainLoopSleep = new Option(0);

    // Evolution
    private final static double defaultMutationFraction = 0.03;
    public final static Option mutationFraction = new Option(defaultMutationFraction);
    public final static Option minMutationFraction = new Option(0.001);
    public final static Option maxMutationFraction = new Option(defaultMutationFraction);
    public final static Option mutationFractionModificationPeriod = new Option(100);
    public final static Option mutationFractionExponent = new Option(-1.0 / 1000000.0);

    // Fitness
    public final static Option cycleCostFactor = new Option(25.0);
    public final static Option distanceRewardFactor = new Option(50.0);
    public final static Option collideCostFactor = new Option(200000.0);
    public final static Option linearAccelerationCostFactor = new Option(0.1);
    public final static Option angularAccelerationCostFactor = new Option(1.0);

    public final static Option travelledDistanceSavePointSteps = new Option(500);
    public final static Option travelledDistanceSavePointDistance = new Option(250);

    // LifeGene
    public final static Option minOldAge = new Option(6000);
    public final static Option maxOldAge = new Option(7000);
    public final static Option oldAgeMutationRate = new Option(0.05);

    // RoundSimObstacle
    public final static Option minRoundObstacleSize = new Option(2);
    public final static Option maxRoundObstacleSize = new Option(18);
    public final static Option minGrowthPercentage = new Option(0.00001);
    public final static Option maxGrowthPercentage = new Option(0.00005);
    public final static Option roundObstaclePopulationSize = new Option(24 * 8);

    // SimRobot
    public final static Option sizeOption = new Option(12);
    public final static Option initialEnergy = new Option(8);
    public final static Option populationSize = new Option(16 * 8);

    // SensorGene
    public final static Option minViewDistance = new Option(6 * 8);
    public final static Option maxViewDistance = new Option(10 * 8);
    public final static Option minFieldOfView = new Option(Math.PI * 0.6);
    public final static Option maxFieldOfView = new Option(Math.PI);

    private final static double sensorMutationRates = defaultMutationRate * 1.0;
    private final static double sensorReplacementRates = defaultReplacementRate * 1.0;
    public final static Option viewDistanceMutationRate = new Option(sensorMutationRates);
    public final static Option viewDistanceReplacementRate = new Option(sensorReplacementRates);
    public final static Option fieldOfViewMutationRate = new Option(sensorMutationRates);
    public final static Option fieldOfViewReplacementRate = new Option(sensorReplacementRates);

    // MovementGene
    public final static Option linearFriction = new Option(0.4); // 0.7, 0.06
    public final static Option angularFriction = new Option(0.8); // 0.5

    private static double calculateForce(double maxVelocity, double friction) {
        return maxVelocity * friction;
    }
    private static double minA = calculateForce(0.01, angularFriction.get()); // 0.1
    private static double maxA = calculateForce(1.0, angularFriction.get()); // 1.0
    private static double minL = calculateForce(0.1, linearFriction.get());
    private static double maxL = calculateForce(3.5, linearFriction.get());

    public final static Option minAngularForce = new Option(minA);
    public final static Option maxAngularForce = new Option(maxA);
    public final static Option minLinearForce = new Option(minL);
    public final static Option maxLinearForce = new Option(maxL);

    private final static double movementMutationRates = defaultMutationRate * 1.0;
    private final static double movementReplacementRates = defaultReplacementRate * 1.0;
    public final static Option angularForceMutationRate = new Option(movementMutationRates);
    public final static Option angularForceReplacementRate = new Option(movementReplacementRates);
    public final static Option linearForceMutationRate = new Option(movementMutationRates);
    public final static Option linearForceReplacementRate = new Option(movementReplacementRates);

    // BrainGene
    public static boolean brainIsRecurrent = false;

    public final static Option minHiddenLayers = new Option(1);
    public final static Option maxHiddenLayers = new Option(3);

    public final static Option neuronMutationRate = new Option(0.5);
    public final static Option neuronReplacementRate = new Option(0.005);

    // NeuronGene
    public final static Option minThreshold = new Option(0.1);
    public final static Option maxThreshold = new Option(1.0);
    public final static Option maxRelaxation = new Option(0.99);

    private final static double neuronMutationRates = defaultMutationRate * 1.0;
    private final static double neuronReplacementRates = defaultReplacementRate * 1.0;
    public final static Option thresholdMutationRate = new Option(neuronMutationRates);
    public final static Option thresholdReplacementRate = new Option(neuronReplacementRates);
    public final static Option relaxationMutationRate = new Option(neuronMutationRates);
    public final static Option relaxationReplacementRate = new Option(neuronReplacementRates);

    // AxonGene
    public final static Option maxStrength = new Option(0.8);
    public final static Option minStrengthening = new Option(0.000001);
    public final static Option maxStrengthening = new Option(0.00002);
    public final static Option minWeakening = new Option(0.000001);
    public final static Option maxWeakening = new Option(0.000005);

    private final static double axonMutationRates = defaultMutationRate * 0.5;
    private final static double axonReplacementRates = defaultReplacementRate * 0.5;
    public final static Option strengthMutationRate = new Option(axonMutationRates);
    public final static Option strengthReplacementRate = new Option(axonReplacementRates);
    public final static Option strengtheningMutationRate = new Option(axonMutationRates);
    public final static Option strengtheningReplacementRate = new Option(axonReplacementRates);
    public final static Option weakeningMutationRate = new Option(axonMutationRates);
    public final static Option weakeningReplacementRate = new Option(axonReplacementRates);
}