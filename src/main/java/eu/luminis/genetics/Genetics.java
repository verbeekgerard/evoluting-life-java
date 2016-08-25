package eu.luminis.genetics;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.luminis.util.Range;

public class Genetics {

	// Creates two child eu.luminis.entities by crossing over the genomes of a and b.
    public List<? extends Gene> mate(Gene a, Gene b) {

    	List<Gene> children = new ArrayList<>();
    	
    	try {
    		Method getInitiateMethod = a.getClass().getMethod("getInitiateProperties");
    		List<String> properties = (List<String>) getInitiateMethod.invoke(a);
    		
    		List<List<Double>> childPropertyValues = new ArrayList<>(Arrays.asList(new ArrayList<>() , new ArrayList<>()));

            for (int j=0; j<properties.size(); j++) {
            	List<Gene> AorB = getAorBRandom(a, b);

            	for (int i=0; i<2; i++) {
	            	Field field = a.getClass().getDeclaredField(properties.get(j));
	            	double value = (double) field.get(AorB.get(i));
	
	            	childPropertyValues.get(i).add(value);
		        }
            }
            
            for (int i=0; i<2 ; i++) {
	            Method initiateMethod = a.getClass().getMethod("initiate", List.class);
	            children.add((Gene) initiateMethod.invoke(a, childPropertyValues.get(i)));
            }
	        
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

        return children;
    };
    
    private ArrayList<Gene> getAorBRandom(Gene a, Gene b){
    	if (new Range(0, 100).random() > 50) {
    		return new ArrayList<>(Arrays.asList(a, b));
    	}
    	else {
    		return new ArrayList<>(Arrays.asList(b, a));
    	}
    }
}