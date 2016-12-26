package eu.luminis.genetics;

import eu.luminis.Options;
import eu.luminis.util.Range;

class AxonGeneEvolver extends Evolver {
    public final EvolvingValue Strength = new EvolvingValue(
            Options.strengthMutationRate,
            Options.strengthReplacementRate,
            Options.mutationFraction,
            new Range(-1 * Options.maxStrength.get(), Options.maxStrength.get()));

    public final EvolvingValue Strengthening = new EvolvingValue(
            Options.strengtheningMutationRate,
            Options.strengtheningReplacementRate,
            Options.mutationFraction,
            new Range(Options.minStrengthening.get(), Options.maxStrengthening.get()));

    public final EvolvingValue Weakening = new EvolvingValue(
            Options.weakeningMutationRate,
            Options.weakeningReplacementRate,
            Options.mutationFraction,
            new Range(Options.minWeakening.get(), Options.maxWeakening.get()));
}
