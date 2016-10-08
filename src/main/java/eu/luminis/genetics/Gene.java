package eu.luminis.genetics;

import java.util.List;

public abstract class Gene {
    public abstract List<String> getInitiatePropertyNames();
    public abstract Gene initiate(List<Double> propertyNames);
}