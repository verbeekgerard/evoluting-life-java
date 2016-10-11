package eu.luminis.general;

import eu.luminis.util.Range;

import java.util.Observable;
import java.util.Observer;

public class MutationFractionModifier implements Observer {
    private Range mutationRange = new Range(Options.minMutationFraction.get(), Options.maxMutationFraction.get());
    private int modificationPeriod = (int)Options.mutationFractionModificationPeriod.get();
    private double mutationFractionExponent = Options.mutationFractionExponent.get();

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
            double currentMutationFraction = Options.mutationFraction.get();
            double newCandidate = currentMutationFraction * Math.exp(mutationFractionExponent * periods);
            double newMutationFraction = mutationRange.assureFlippedBounds(newCandidate);
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