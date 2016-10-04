package eu.luminis.export;

import eu.luminis.entities.Animal;
import eu.luminis.ui.Stats;

import java.util.List;

public interface ExportInfo {
    String getHealth();
    Stats getStats();
    List<Animal> getAnimals();
}
