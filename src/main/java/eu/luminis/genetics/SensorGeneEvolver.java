package eu.luminis.genetics;

import eu.luminis.Options;
import eu.luminis.util.Range;

class SensorGeneEvolver extends Evolver {
    public final EvolvingValue ViewDistance = new EvolvingValue(
            Options.viewDistanceMutationRate,
            Options.viewDistanceReplacementRate,
            Options.mutationFraction,
            new Range(Options.minViewDistance.get(), Options.maxViewDistance.get()));

    public final EvolvingValue FieldOfView = new EvolvingValue(
            Options.fieldOfViewMutationRate,
            Options.fieldOfViewReplacementRate,
            Options.mutationFraction,
            new Range(Options.minFieldOfView.get(), Options.maxFieldOfView.get()));
}
