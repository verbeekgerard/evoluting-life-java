package eu.luminis.export;

import com.google.gson.Gson;
import eu.luminis.Simulation;

public class PopulationExporter  {
    
    private static Simulation simulation = Simulation.getInstance();

    public String export() {
        return new Gson().toJson(simulation.exportPopulation());
    }
}
