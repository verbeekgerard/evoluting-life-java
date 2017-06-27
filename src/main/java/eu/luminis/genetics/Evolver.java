package eu.luminis.genetics;

import eu.luminis.util.Range;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Evolver {

    // Creates two children by crossing over the genomes of a and b.
    public <TGene extends Gene> TGene[] mate(TGene a, TGene b) {
        double[] propertiesA = a.getInitiateProperties();
        double[] propertiesB = b.getInitiateProperties();

        double[][] childrenProperties = initializeChildrenProperties(propertiesA.length);

        for (int j=0; j<propertiesA.length; j++) {
            double[][] AorB = getAorBRandom(propertiesA, propertiesB);

            for (int i=0; i<2; i++) {
                double[] aOrB = AorB[i];
                double value = aOrB[j];
                childrenProperties[i][j] = value;
            }
        }

        TGene[] genes = a.newArray(2);
        for (int i=0; i<2; i++) {
            TGene gene = a.initiate(childrenProperties[i]);
            genes[i] = gene;
        }

        return genes;
    }

    private double[][] getAorBRandom(double[] a, double[] b) {
        if (new Range(0, 100).random() < 50) {
            return new double[][]{a, b};
        } else {
            return new double[][]{b, a};
        }
    }

    private double[][] initializeChildrenProperties(int propertiesCount) {
        double[][] childrenProperties = new double[2][];

        for (int i=0; i<2; i++) {
            double[] properties = new double[propertiesCount];
            childrenProperties[i] = properties;
        }

        return childrenProperties;
    }
}