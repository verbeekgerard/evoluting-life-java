package eu.luminis.genetics;

import java.util.Map;

public abstract class Gene {
    public abstract Map<String, Double> getInitiateProperties();
    public abstract Gene initiate(Map<String, Double> properties);
}