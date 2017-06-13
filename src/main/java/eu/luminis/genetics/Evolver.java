package eu.luminis.genetics;

import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Evolver {

    // Creates two children by crossing over the genomes of a and b.
    public <TGene extends Gene> List<TGene> mate(TGene a, TGene b) {
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

        return childrenProperties.stream()
                .map(childProperties -> (TGene) a.initiate(childProperties))
                .collect(Collectors.toList());
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