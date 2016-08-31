package eu.luminis.export;

import eu.luminis.entities.Animal;
import eu.luminis.ui.Stats;

import java.util.List;

/**
 * Created by gerardverbeek on 25/08/16.
 */
public interface ExportInfo {
    String getHealth();
    Stats getStats();
    List<Animal> getAnimals();
}
