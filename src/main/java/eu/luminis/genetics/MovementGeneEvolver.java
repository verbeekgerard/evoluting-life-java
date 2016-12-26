package eu.luminis.genetics;

import eu.luminis.Options;
import eu.luminis.util.Range;

class MovementGeneEvolver extends Evolver {
    public final EvolvingValue AngularForce = new EvolvingValue(
            Options.angularForceMutationRate,
            Options.angularForceReplacementRate,
            Options.mutationFraction,
            new Range(Options.minAngularForce.get(), Options.maxAngularForce.get()));

    public final EvolvingValue LinearForce = new EvolvingValue(
            Options.linearForceMutationRate,
            Options.linearForceReplacementRate,
            Options.mutationFraction,
            new Range(Options.minLinearForce.get(), Options.maxLinearForce.get()));
}
