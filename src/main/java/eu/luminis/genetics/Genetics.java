package eu.luminis.genetics;

import java.util.*;
import eu.luminis.util.Range;

public class Genetics {

	// Creates two child by crossing over the genomes of a and b.
    public List<? extends Gene> mate(Gene a, Gene b) {
        Map<String, Double> mapA = a.getInitiateProperties();
        Map<String, Double> mapB = b.getInitiateProperties();

        List<Map<String, Double>> childrenProperties = initializeChildrenProperties();

        for (String propertyName : mapA.keySet()) {
            List<Map<String, Double>> AorB = getAorBRandom(mapA, mapB);

            for (int i=0; i<2; i++) {
                Map<String, Double> aOrB = AorB.get(i);
                double value = aOrB.get(propertyName);
                childrenProperties.get(i).put(propertyName, value);
            }
        }

        List<Gene> children = new ArrayList<>();
        for (int i=0; i<2 ; i++) {
            children.add(a.initiate(childrenProperties.get(i)));
        }

        return children;
    }

    private <T> ArrayList<T> getAorBRandom(T a, T b){
    	if (new Range(0, 100).random() > 50) {
    		return new ArrayList<>(Arrays.asList(a, b));
    	}
    	else {
    		return new ArrayList<>(Arrays.asList(b, a));
    	}
    }

    private List<Map<String, Double>> initializeChildrenProperties() {
        List<Map<String, Double>> childrenProperties = new ArrayList<>();
        childrenProperties.add(new HashMap<>());
        childrenProperties.add(new HashMap<>());

        return childrenProperties;
    }
}