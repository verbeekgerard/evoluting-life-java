package eu.luminis.genetics;

import eu.luminis.Options;
import eu.luminis.util.Range;

class LayerGeneEvolver extends Evolver {
    public final EvolvingValue Weight = new EvolvingValue(
            Options.weightMutationRate,
            Options.weightReplacementRate,
            Options.mutationFraction,
            new Range(-1 * Options.maxWeight.get(), Options.maxWeight.get()));

    public final EvolvingValue Bias = new EvolvingValue(
            Options.biasMutationRate,
            Options.biasReplacementRate,
            Options.mutationFraction,
            new Range(-1 * Options.maxBias.get(), Options.maxBias.get()));

        public final EvolvingValue StateWeight = new EvolvingValue(
            Options.stateWeightMutationRate,
            Options.stateWeightReplacementRate,
            Options.mutationFraction,
            new Range(-1 * Options.maxStateWeight.get(), Options.maxStateWeight.get()));
    }
