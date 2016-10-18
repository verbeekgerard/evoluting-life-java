package eu.luminis.robots;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import eu.luminis.entities.Position;
import eu.luminis.evolution.RouletteWheelSelectionByRank;
import eu.luminis.general.EventBroadcaster;
import eu.luminis.general.EventType;
import eu.luminis.general.Options;
import eu.luminis.genetics.Genome;
import eu.luminis.util.Range;

public class SimRobotPopulation {
	
	private List<SimRobot> entities = new CopyOnWriteArrayList<>(); // Slow lost but no exceptions in UI
	private SimRobot winningEntity;
	private double populationSize;
	private SimWorld simWorld;
	
	public SimRobotPopulation(SimWorld simWorld) {
		this.populationSize = Options.populationSize.get();
		this.simWorld = simWorld;
		
		// Fill our population with eu.luminis.entities
		for (int i = 0; i < populationSize; i++) {
			Genome genome = createGenome();
			Position position = createRandomPosition();
			SimRobot entity = new SimRobot(genome, position, simWorld);
			// this.entityCreated.notifyAsync(newAnimal);
			this.entities.add(entity);
		}
	}
	
    public List<SimRobot> getAllRobots() {
    	return this.entities;
    }

    public SimRobot getWinningEntity() {
        return winningEntity;
    }
    
    public void run() {
		Collections.sort(this.entities);
		// Find the best ranking entity
		winningEntity = this.entities.get(0);

		List<SimRobot> entitiesToRemove = new ArrayList<>();
		for (SimRobot entity : entities) {
	
			entity.run();

			// Check entity life cycle and remove dead eu.luminis.entities
			if (!entity.lives()) {
				entitiesToRemove.add(entity);
			}
		}

		for (int i = 0; i < entitiesToRemove.size() / 2; i++) {
			List<SimRobot> parents = selectParents();
			produceChildren(parents);
		}

		for (SimRobot entityToRemove : entitiesToRemove) {
			this.entities.remove(entityToRemove);
		}

		while (this.entities.size() <= populationSize - 2) {
			List<SimRobot> parents = selectParents();
			produceChildren(parents);
		}
	}
    
    public void importPopulation(String json) {
		this.entities = new CopyOnWriteArrayList<>();
		
		Type listType = new TypeToken<ArrayList<Genome>>(){}.getType();
		List<Genome> genomes = new Gson().fromJson(json, listType);
		
		for (Genome genome : genomes) {
			SimRobot animal = new SimRobot(genome, createRandomPosition(), simWorld);
			this.entities.add(animal);
		}
	}
	
	public String exportPopulation() {
		ArrayList<Genome> genomes = entities.stream()
				.map(SimRobot::getGenome)
				.collect(Collectors.toCollection(ArrayList::new));

		return new Gson().toJson(genomes);
	}

    private Genome createGenome() {
        return new Genome(BrainInput.getNodesCount(), BrainOutput.getNodesCount());
    }

    private Position createRandomPosition() {
        Range range = new Range(-0.98, 0.98);
        double x = simWorld.getWidth() /2 + simWorld.getWidth() /2 * range.random();
        double y = simWorld.getHeight() /2 + simWorld.getHeight() /2 * range.random();
        return new Position(x, y, Math.random() * Math.PI * 2);
    }

    private void produceChildren(List<SimRobot> parents) {
		List<Genome> children = parents.get(0).getGenome().mate(parents.get(1).getGenome());
		for (Genome child : children) {
			child.mutate();

			// Spawn a new entity from it
			Position position = createRandomPosition();
			SimRobot newAnimal = new SimRobot(child, position, simWorld);
			this.entities.add(newAnimal);
			EventBroadcaster.getInstance().broadcast(EventType.NEW_ANIMAL, newAnimal);
		}
	}

	private List<SimRobot> selectParents() {
		List<SimRobot> parents = new ArrayList<>();

		for (int i = 0; i < 2; i++) {
			SimRobot winningEntity = new RouletteWheelSelectionByRank().select(this.entities);
			parents.add(winningEntity);
		}

		return parents;
	}
}
