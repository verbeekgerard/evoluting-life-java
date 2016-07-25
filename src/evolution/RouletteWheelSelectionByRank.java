package evolution;

import java.util.List;

import entities.Animal;
import util.Range;

public class RouletteWheelSelectionByRank {
	
	public Animal select(List<Animal> entities) {
		long scoreCount = entities.size();
		
	    double randomOccurenceSum = new Range(1l, scoreCount * (scoreCount+1) / 2 ).random();

        for ( int i = 0; i < scoreCount; i++ ) {
            long occurence = scoreCount - i - 1;
            long occurenceSum = occurence * (occurence+1) / 2;

            if (randomOccurenceSum > occurenceSum){
            	return entities.get(i);
            }
        }

        throw new RuntimeException("rouletteWheelSelectionByRank didn't select a score: " + randomOccurenceSum);
	}
}

/*
Scaled Rank

SP = Selective Pressure (2.0 >= SP >= 1.0, so that SP is the selective pressure of the fittest and 2-SP is the selective pressure of the weakest)
n = population size
Pos = position of an individual sorted by score (Pos=1 is the weakest, Pos=n is the fittest)

Rank (Pos) = 2-SP + ( 2*(SP-1) * (Pos-1)/(n-1) )
*/