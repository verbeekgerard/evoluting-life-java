package evolution;

import java.util.ArrayList;
import java.util.List;

import entities.Animal;

public class RouletteWheelSelectionByRank {
	
	public Animal select(List<Animal> entities) {
	    List<Score> weightedList = new ArrayList<>();
	    int randomIndex = (int) Math.floor( Math.random() * entities.size() * (entities.size()+1) / 2 );
	
	    for ( int i = 0; i < entities.size(); i++ ) {
	    	Animal entity = entities.get(i); 
	        Score score = new Score(entity, entity.rank());
	        int occurence = entities.size() - i;
	        score.occurenceSum = occurence * (occurence+1) / 2;
	
	        if (score.occurenceSum < randomIndex) {
	        	return weightedList.get(i-1).context; 
	        } // Test this for the edge cases!
	        
	        weightedList.add( score );
	    }
	    
	    return weightedList.get(weightedList.size()-1).context;
	}
}

	/*
	rank based would be using: n(n+1)/2
	For instance, in a population of ten individuals, 
	the chance of the first-ranked individual is 10 to 55, the chance of the 10th individual is 1 to 55

	rank  occ   occSum
	0     10    55
	1      9    45
	2      8    36
	3      7    28
	4      6    21
	5      5    15
	6      4    10
	7      3    6
	8      2    3
	9      1    1
	*/