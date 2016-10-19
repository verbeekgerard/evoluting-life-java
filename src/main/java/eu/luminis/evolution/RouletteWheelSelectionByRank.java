package eu.luminis.evolution;

import java.util.List;

import eu.luminis.entities.Animal;
import eu.luminis.robots.SimObstacle;
import eu.luminis.robots.SimRobot;
import eu.luminis.util.Range;

public class RouletteWheelSelectionByRank {
	
	public SimObstacle select(List<? extends SimObstacle> entities) {
		long scoreCount = entities.size();
		
	    double randomOccurrenceSum = new Range(1l, scoreCount * (scoreCount+1) / 2 ).random();

        for ( int i = 0; i < scoreCount; i++ ) {
            long occurrence = scoreCount - i - 1;
            long occurrenceSum = occurrence * (occurrence+1) / 2;

            if (randomOccurrenceSum > occurrenceSum) {
            	return entities.get(i);
            }
        }

        throw new RuntimeException("rouletteWheelSelectionByRank didn't select a score: " + randomOccurrenceSum);
	}
}

/*
Scaled Rank

SP = Selective Pressure (2.0 >= SP >= 1.0, so that SP is the selective pressure of the fittest and 2-SP is the selective pressure of the weakest)
n = population size
Pos = position of an individual sorted by score (Pos=1 is the weakest, Pos=n is the fittest)

Rank (Pos) = 2-SP + ( 2*(SP-1) * (Pos-1)/(n-1) )
*/
