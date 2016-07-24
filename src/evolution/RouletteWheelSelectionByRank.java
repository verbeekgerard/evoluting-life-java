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