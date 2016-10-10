package eu.luminis.general;

import java.util.Observable;
import java.util.Observer;

public class MutationFractionModifier implements Observer {
    private int iterationPeriod = (int)Options.mutationFractionCyclesPeriod.get();
    private double periodicFactor = Options.mutationFractionPeriodicFactor.get();
    private int iteration = 0;

    @Override
    public void update(Observable o, Object arg) {
        Event event = (Event) arg;
        if (event.type.equals(EventType.CYCLE_END)) {
            processCycleEnd();
        }
    }

    private void processCycleEnd() {
        if (isNewPeriod()) {
            double currentValue = Options.mutationFraction.get();
            Options.mutationFraction.set(currentValue * periodicFactor);
        }
    }

    private boolean isNewPeriod() {
        if (iteration++ > iterationPeriod) {
            iteration = 0;
            return true;
        }

        return false;
    }
}