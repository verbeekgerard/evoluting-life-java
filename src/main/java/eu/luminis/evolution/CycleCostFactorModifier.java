package eu.luminis.evolution;

import eu.luminis.Options;
import eu.luminis.events.Event;
import eu.luminis.events.EventType;
import eu.luminis.robots.sim.IAgeRetriever;
import eu.luminis.robots.sim.SimRobotPopulation;
import eu.luminis.util.Option;

import java.util.Observable;
import java.util.Observer;

public class CycleCostFactorModifier implements Observer {
    private static final Option cycleCostFactor = Options.cycleCostFactor;
    private SimRobotPopulation population;

    public CycleCostFactorModifier(SimRobotPopulation population) {
        this.population = population;
    }

    @Override
    public void update(Observable o, Object arg) {
        Event event = (Event) arg;
        if (event.type.equals(EventType.CYCLE_END)) {
            processCycleEnd();
        }
    }

    private void processCycleEnd() {
        IAgeRetriever winner = population.getWinningEntity().getAgeInformation();

        if (winner.getAge() < winner.getOldAge()) {
            return;
        }

        double currentFactor = cycleCostFactor.get();
        cycleCostFactor.set(currentFactor + 0.001);
    }
}
