package eu.luminis.genetics;

import java.util.List;

abstract class Gene {
    public abstract List<Double> getInitiateProperties();
    public abstract Gene initiate(List<Double> properties);
}