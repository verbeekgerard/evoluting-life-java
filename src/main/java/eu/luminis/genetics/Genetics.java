package eu.luminis.genetics;

import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Genetics {

	// Creates two child by crossing over the genomes of a and b.
    public List<? extends Gene> mate(Gene a, Gene b) {
        List<Double> propertiesA = a.getInitiateProperties();
        List<Double> propertiesB = b.getInitiateProperties();

        List<List<Double>> childrenProperties = initializeChildrenProperties();

        for (int j=0; j<propertiesA.size(); j++) {
            List<List<Double>> AorB = getAorBRandom(propertiesA, propertiesB);

            for (int i=0; i<2; i++) {
                List<Double> aOrB = AorB.get(i);
                double value = aOrB.get(j);
                childrenProperties.get(i).add(value);
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

    private List<List<Double>> initializeChildrenProperties() {
        List<List<Double>> childrenProperties = new ArrayList<>();
        childrenProperties.add(new ArrayList<>());
        childrenProperties.add(new ArrayList<>());

        return childrenProperties;
    }
}