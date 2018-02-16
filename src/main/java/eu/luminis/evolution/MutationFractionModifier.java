package eu.luminis.evolution;

import eu.luminis.Options;
import eu.luminis.events.Event;
import eu.luminis.events.EventType;
import eu.luminis.util.Range;

import java.util.Observable;
import java.util.Observer;

public class MutationFractionModifier implements Observer {
    private static final Range mutationRange = new Range(Options.minMutationFraction.get(), Options.maxMutationFraction.get());
    private static final int modificationPeriod = (int)Options.mutationFractionModificationPeriod.get();
    private static final double mutationFractionExponent = Options.mutationFractionExponent.get();
    private static final double maxMutationFraction = Options.maxMutationFraction.get();

    private int periods = 0;
    private int iteration = 0;

    public MutationFractionModifier() {
        Options.mutationFraction.set(mutationRange.getUpperBound());
    }

    @Override
    public void update(Observable o, Object arg) {
        Event event = (Event) arg;
        if (event.type.equals(EventType.CYCLE_END)) {
            processCycleEnd();
        }
    }

    private void processCycleEnd() {
        if (isNewPeriod()) {
            double newCandidate = maxMutationFraction * Math.exp(periods * mutationFractionExponent);
            double newMutationFraction = mutationRange.assureFlippedBounds(newCandidate);

            double currentMutationFraction = Options.mutationFraction.get();
            Options.mutationFraction.set(newMutationFraction);

            periods = newMutationFraction > currentMutationFraction ? 0 : periods + 1;
        }
    }

    private boolean isNewPeriod() {
        if (iteration++ > modificationPeriod) {
            iteration = 0;
            return true;
        }

        return false;
    }
}