package eu.luminis.genetics;

import eu.luminis.Options;
import eu.luminis.util.Range;

class NeuronGeneEvolver extends Evolver {
    public final EvolvingValue Threshold = new EvolvingValue(
            Options.thresholdMutationRate,
            Options.thresholdReplacementRate,
            Options.mutationFraction,
            new Range(Options.minThreshold.get(), Options.maxThreshold.get()));

    public final EvolvingValue Relaxation = new EvolvingValue(
            Options.relaxationMutationRate,
            Options.relaxationReplacementRate,
            Options.mutationFraction,
            new Range(0, Options.maxRelaxation.get()));
}
