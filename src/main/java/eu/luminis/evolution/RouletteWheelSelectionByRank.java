package eu.luminis.evolution;

import java.util.List;

import eu.luminis.robots.sim.SimObstacle;
import eu.luminis.util.Range;

public class RouletteWheelSelectionByRank {
	
	public SimObstacle select(List<? extends SimObstacle> entities) {
		long populationSize = entities.size();

		long maxPoolIndex = calculateMaxPoolIndex(populationSize);
	    double randomPoolIndex = new Range(1, maxPoolIndex).random();

        for ( int i = 0; i < populationSize; i++ ) {
            long nextMaxPoolIndex = calculateMaxPoolIndex(populationSize - i - 1);

            if (randomPoolIndex > nextMaxPoolIndex) {
            	return entities.get(i);
            }
        }

        throw new RuntimeException("rouletteWheelSelectionByRank didn't select a score: " + randomPoolIndex);
	}

	private long calculateMaxPoolIndex(long rank) {
	    return rank * (rank + 1) / 2;
    }
}

/*
Scaled Rank

SP = Selective Pressure (2.0 >= SP >= 1.0, so that SP is the selective pressure of the fittest and 2-SP is the selective pressure of the weakest)
n = population size
Pos = position of an individual sorted by score (Pos=1 is the weakest, Pos=n is the fittest)

Rank (Pos) = 2-SP + ( 2*(SP-1) * (Pos-1)/(n-1) )
*/
