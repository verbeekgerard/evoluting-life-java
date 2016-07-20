package genetics;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import entities.Organism;

public class Genetics {

//	var permutationsCollection = [];
//	
//	public Object getA(Object a, Object b) { return a; }
//    public Object getB(Object a, Object b) { return b; }
//	
 // Gets an Array representation of the mateState of the entity.
//    public  convertToStateArray(entity) {
//        var mateState = entity.getMateState();
//
//        var stateArray = [];
//        for (var propertyName in mateState) {
//            stateArray.push( mateState[propertyName] );
//        }
//
//        return stateArray;
//    }
//    
// // Combines the propertyNames with their values.
//    public convertToMateState(array, propertyNames) {
//        var mateState = {};
//
//        for (var i = 0; i < array.length; i++) {
//            mateState[propertyNames[i]] = array[i];
//        }
//
//        return mateState;
//    }
//    
// // Gets the propertyNames of the mateState.
//    function getPropertyNameArray(entity) {
//        var mateState = entity.getMateState();
//
//        var propertyNames = [];
//        for (var propertyName in mateState) {
//            propertyNames.push( propertyName );
//        }
//
//        return propertyNames;
//    }
    
	// Creates two child entities by crossing over the genomes of a and b.
    public List<? extends Gene> mate(Gene a, Gene b) { //createCallback

    	List<Gene> children = new ArrayList<>();
    	
    	List<Gene> permutationPair = new ArrayList<>();
    	permutationPair.add(a);
    	permutationPair.add(b); // TODO random permutation pair
    	
//        var values = permutationsCollection[propertyCount].getRandomPermutationPair();
//        var children = [];
    	
    	

    	try {
    		Method getInitiateMethod = a.getClass().getMethod("getInitiateProperties");
    		List<String> properties = (List<String>) getInitiateMethod.invoke(a);
    		
	        for (int i=0; i<2; i++) {
	//            var stateArray = [];
	        	List<Double> values = new ArrayList<>();
	            for (int j=0; j<properties.size(); j++) {
	            	
	            	Field field = permutationPair.get(i).getClass().getDeclaredField(properties.get(j));
	            	double value = (double) field.get(permutationPair.get(i));
	//            	MovementGene AorB = permutationPair.get(i).getClass().getDeclaredField();;

	            	values.add(value);
	            }
	            
	            Method initiateMethod = a.getClass().getMethod("initiate", List.class);
	            children.add((Gene) initiateMethod.invoke(a, values));
//	            children.add(MovementGene.initiate(values));
	            
	//            var mateState = convertToMateState(stateArray, propertyNames);
	//            children.push( createCallback(mateState) );
	            
	    
	        }
	        
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

        return children;
    };
    
}