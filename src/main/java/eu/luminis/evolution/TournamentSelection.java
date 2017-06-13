package eu.luminis.evolution;

import eu.luminis.robots.sim.SimObstacle;

import java.util.List;

public class TournamentSelection {
	
	public SimObstacle select(List<? extends SimObstacle> entities) {
		int k = 3; // TODO: should be an option or so...

		int populationSize = entities.size();
        int index = populationSize - 1;

		for (int i=0; i<k; i++) {
		    int randomIndex = (int)Math.floor(Math.random() * populationSize);
            if (randomIndex < index) { // The population has been sorted ny fitness
                index = randomIndex;
            }
        }

        return entities.get(index);
	}
}
