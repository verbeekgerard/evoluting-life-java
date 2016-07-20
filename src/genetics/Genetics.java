package genetics;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import util.Range;

public class Genetics {

	// Creates two child entities by crossing over the genomes of a and b.
    public List<? extends Gene> mate(Gene a, Gene b) {

    	List<Gene> children = new ArrayList<>();
    	
    	List<Gene> permutationPair = new ArrayList<>();
    	
    	if (new Range(0, 100).random() > 50){
    		permutationPair.add(a);
    	}
    	else {
    		permutationPair.add(b); 
    	}
    	if (new Range(0, 100).random() > 50){
    		permutationPair.add(a);
    	}
    	else {
    		permutationPair.add(b); 
    	}
    	
    	try {
    		Method getInitiateMethod = a.getClass().getMethod("getInitiateProperties");
    		List<String> properties = (List<String>) getInitiateMethod.invoke(a);
    		
	        for (int i=0; i<2; i++) {

	        	List<Double> values = new ArrayList<>();
	            for (int j=0; j<properties.size(); j++) {
	            	
	            	Field field = permutationPair.get(i).getClass().getDeclaredField(properties.get(j));
	            	double value = (double) field.get(permutationPair.get(i));

	            	values.add(value);
	            }
	            
	            Method initiateMethod = a.getClass().getMethod("initiate", List.class);
	            children.add((Gene) initiateMethod.invoke(a, values));

	        }
	        
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

        return children;
    };
    
}