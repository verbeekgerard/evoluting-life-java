package eu.luminis.genetics;

import java.util.List;

public abstract class Gene {
    public abstract List<Double> getInitiateProperties();
    public abstract Gene initiate(List<Double> properties);
}