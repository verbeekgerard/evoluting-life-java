package eu.luminis.genetics;

import eu.luminis.Options;
import eu.luminis.util.Option;
import eu.luminis.util.Range;

class LifeGeneEvolver extends Evolver {
    public final EvolvingValue OldAge = new EvolvingValue(
            Options.oldAgeMutationRate,
            new Option(0.0),
            Options.mutationFraction,
            new Range(Options.minOldAge.get(), Options.maxOldAge.get()));
}
